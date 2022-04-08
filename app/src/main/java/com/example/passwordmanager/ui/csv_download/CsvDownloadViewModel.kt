package com.example.passwordmanager.ui.csv_download

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.passwordmanager.dao.PassInfoDao
import com.example.passwordmanager.entity.PasswordInfo
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import org.koin.java.KoinJavaComponent.inject
import java.io.FileOutputStream
import java.util.*

class CsvDownloadViewModel(application: Application): AndroidViewModel(application) {
    private val dao by inject<PassInfoDao>(PassInfoDao::class.java)

    fun downloadOnClickListener(fragment: Fragment) = View.OnClickListener {
        val context = fragment.requireContext()

        //ダウンロードディレクトリにファイルを保存するためにおまじない
        val fileName   = generateFileName()
        val epUri      = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val values     = ContentValues().apply { put(MediaStore.Downloads.DISPLAY_NAME, fileName) }
        val contentUri = context.contentResolver.insert(epUri, values)

        // observeってやらないとデータ取得できない
        dao.getAll().observe(fragment.viewLifecycleOwner) { passList ->
            // パスワード情報用のListを取得
            val passwordList: List<List<String>> = PasswordInfo.csvHeader + passList.map(PasswordInfo::toCsv)

            // ファイル保存
            context.contentResolver.openFileDescriptor(contentUri!!, "w", null).use {
                csvWriter().writeAll(passwordList, FileOutputStream(it!!.fileDescriptor))
            }

            // この辺よくわからん
            values.clear()
            values.put(MediaStore.Downloads.IS_PENDING, 0)
            context.contentResolver.update(contentUri, values, null, null)

            // ダウンロードしたことを通知
            Toast.makeText(context, "CSVファイルをダウンロードしました: $fileName", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun generateFileName(): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd")
        val date = Date(System.currentTimeMillis())
        return "password_list_${dateFormat.format(date)}.csv"
    }
}