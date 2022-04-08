package com.example.passwordmanager.ui.csv_upload

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.passwordmanager.dao.PassInfoDao
import com.example.passwordmanager.entity.PasswordInfo
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import io.reactivex.schedulers.Schedulers
import org.koin.java.KoinJavaComponent.inject
import java.io.FileInputStream
import java.io.InputStream

class CsvUploadViewModel(application: Application): AndroidViewModel(application) {
    private val dao by inject<PassInfoDao>(PassInfoDao::class.java)
    private var selectedFile: InputStream? = null
    val liveFileName: MutableLiveData<String> = MutableLiveData("")

    fun activityResultListener(uri: Uri?, context: Context) {
        if(uri == null) {
            if(selectedFile == null) // ファイルが選択されていればToastを出さない
                Toast.makeText(context, "No file selected!", Toast.LENGTH_LONG).show()
            return
        }

        context.contentResolver.run {
            val parcelFileDescriptor = openFileDescriptor(uri, "r")
            selectedFile = FileInputStream(parcelFileDescriptor?.fileDescriptor) // InputStream取得

            query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                val selectFileName = cursor.getString(nameIndex)
                liveFileName.postValue(selectFileName) // ファイル名取得
            }
        }
    }

    fun fileSelectionClickListener(content: ActivityResultLauncher<String>) = View.OnClickListener { button ->
        button.isEnabled = false
        content.launch("*/*")
        button.isEnabled = true
    }

    fun uploadClickListener(context: Context) = View.OnClickListener { button ->
        button.isEnabled = false

        if(selectedFile == null) {
            Toast.makeText(context, "ファイルを選択してください", Toast.LENGTH_SHORT).show()
        } else {
            selectedFile?.let { file ->
                csvReader()
                    .readAll(file)
                    .map { PasswordInfo(0, it[0], it[1], it[2], it[3], it[4], it[4], it[5], it[6], it[7], it[8], it[9], it[10], it[11], it[12]) }
                    .forEach { dao
                        .upsert(it) // 挿入
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                    }
                }
            Toast.makeText(context, "パスワードがCSVファイルから読み込まれました", Toast.LENGTH_SHORT).show()
            clearSelectFile() // 選択したファイルを解除しておく
            // TODO: MainActivityに戻る処理を書いておきたい
            // TODO: Toastを１つしか表示しないようにするかも（メンバにToastを加えて、showするのをそのメンバだけにすればいい）
        }

        button.isEnabled = true
    }

    /** 選択したファイルを解除する */
    private fun clearSelectFile() {
        selectedFile = null
        liveFileName.postValue("")
    }
}