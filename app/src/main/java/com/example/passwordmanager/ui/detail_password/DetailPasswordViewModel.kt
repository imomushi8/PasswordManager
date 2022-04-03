package com.example.passwordmanager.ui.detail_password

import android.annotation.SuppressLint
import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.passwordmanager.R
import com.example.passwordmanager.dao.PassInfoDao
import com.example.passwordmanager.entity.PasswordInfo
import com.example.passwordmanager.ui.edit_password.EditPasswordFragment
import com.example.passwordmanager.ui.edit_password.EditPasswordViewModel
import com.example.passwordmanager.ui.home.HomeFragmentDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.java.KoinJavaComponent.inject

class DetailPasswordViewModel(application: Application): AndroidViewModel(application) {
    companion object {
        const val KEY_DETAIL = "detail_password"
    }

    private val dao: PassInfoDao by inject(PassInfoDao::class.java)
    private val clipboard: ClipboardManager by inject(ClipboardManager::class.java)

    /** パスワード情報のバインディング用変数 */
    val livePassInfo = MutableLiveData(PasswordInfo.default())

    /** bundleからパスワード情報を取得する */
    @SuppressLint("CheckResult")
    fun getResult(bundle: Bundle) {
        when {
            bundle.containsKey(KEY_DETAIL) -> dao
                .find(bundle.getInt(KEY_DETAIL))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(livePassInfo::postValue)
            else -> livePassInfo.postValue(PasswordInfo.default())
        }
    }

    /** 指定のcontentをクリップボートに入れる */
    fun copyText(editText: TextView) =
        clipboard.setPrimaryClip(ClipData.newPlainText("", editText.text.toString()))

    /** 編集ボタン押下時のイベント */
    fun edit(dialog: BottomSheetDialogFragment, navController: NavController) = View.OnClickListener {
        it.isEnabled = false
        dialog.dismiss()
        val id = livePassInfo.value?.id!!
        // safeargsの記述方法 遷移元FragmentDirections.利用するactionID名(渡す変数)の形式
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationEditPassword(id)
        navController.navigate(action)
        it.isEnabled = true
    }
}