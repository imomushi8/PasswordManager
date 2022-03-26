package com.example.passwordmanager.ui.edit_password

import android.annotation.SuppressLint
import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.passwordmanager.dao.PassInfoDao
import com.example.passwordmanager.entity.PasswordInfo
import com.example.passwordmanager.ui.home.HomeRecyclerAdapter
import com.example.passwordmanager.ui.home.HomeViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.java.KoinJavaComponent.inject

class EditPasswordViewModel(application: Application): AndroidViewModel(application) {
    // private val dao = AppDatabase.getInstance(application).passInfoDao()
    // private val clipboard = application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    private val dao: PassInfoDao by inject(PassInfoDao::class.java)
    private val clipboard: ClipboardManager by inject(ClipboardManager::class.java)

    /** パスワード情報の双方向バインディング用変数 */
    val livePassInfo = MutableLiveData(PasswordInfo.default())

    /** ユーザーIDにメアドを使うかどうかのフラグ */
    private var useEmailToUserId: Boolean = false

    /** ユーザー名にメアドを使うかどうかのフラグ */
    private var useEmailToUserName: Boolean = false

    fun initialize() {
        livePassInfo.postValue(PasswordInfo.default()) // パスワード情報の初期化
    }

    /** bundleからパスワード情報を取得する */
    @SuppressLint("CheckResult")
    fun getResult(bundle: Bundle) {
        when {
            bundle.containsKey(HomeViewHolder.RESULT_KEY) -> {
                dao
                    .find(bundle.getInt(HomeViewHolder.RESULT_KEY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(livePassInfo::postValue)
            }
            else -> initialize()
        }
    }

    /** ユーザーIDにメアドを使うかどうかスイッチングする */
    fun switchUserIdFlag(view: View, isUseFlag: Boolean) {
        view.isEnabled = !isUseFlag
        useEmailToUserId = isUseFlag
    }

    /** ユーザー名にメアドを使うかどうかスイッチングする */
    fun switchUserNameFlag(view: View, isUseFlag: Boolean) {
        view.isEnabled = !isUseFlag
        useEmailToUserName = isUseFlag
    }

    /** 指定のcontentをクリップボートに入れる */
    fun copyText(label: String, editText: EditText) =
        clipboard.setPrimaryClip(ClipData.newPlainText(label, editText.text.toString()))

    /** 登録ボタン押下時のイベント */
    fun register() {
        val entity = livePassInfo.value!!.copy().apply { // @=でつなぐと双方向バインディングになるので、これを使う
            if (useEmailToUserId) user_id = e_mail // IDにメアドを使う場合
            if (useEmailToUserName) user_name = e_mail // ユーザー名にメアドを使う場合
        }
        dao
            .upsert(entity) // insertまたはupdate
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}