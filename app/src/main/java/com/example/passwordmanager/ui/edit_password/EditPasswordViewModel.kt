package com.example.passwordmanager.ui.edit_password

import android.annotation.SuppressLint
import android.app.Application
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.passwordmanager.dao.PassInfoDao
import com.example.passwordmanager.entity.PasswordInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.java.KoinJavaComponent.inject

class EditPasswordViewModel(application: Application): AndroidViewModel(application) {

    private val dao: PassInfoDao by inject(PassInfoDao::class.java)

    /** パスワード情報の双方向バインディング用変数 */
    val livePassInfo = MutableLiveData(PasswordInfo.default())

    /** ユーザーIDにメアドを使うかどうかのフラグ */
    private var useEmailToUserId: Boolean = false

    /** ユーザー名にメアドを使うかどうかのフラグ */
    private var useEmailToUserName: Boolean = false

    /** bundleからパスワード情報を取得する */
    @SuppressLint("CheckResult")
    fun getPassInfo(args: EditPasswordFragmentArgs) {
        when(args.id) {
            0    -> return
            else -> dao
                .find(args.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(livePassInfo::postValue)
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

    /** 登録ボタン押下時のイベント */
    fun register(navController: NavController) {
        val entity = livePassInfo.value!!.copy().apply { // @=でつなぐと双方向バインディングになるので、これを使う
            if (useEmailToUserId) user_id = e_mail // IDにメアドを使う場合
            if (useEmailToUserName) user_name = e_mail // ユーザー名にメアドを使う場合
        }
        dao
            .upsert(entity) // insertまたはupdate
            .subscribeOn(Schedulers.io())
            .subscribe()

        navController.popBackStack() // backstackからホーム画面に戻る（今回限りの特殊実装かも）
    }

    /** システムのバックキーを押下したときの動作 */
    fun backKeyPressedCallback(navController: NavController) = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navController.popBackStack()
        }
    }
}