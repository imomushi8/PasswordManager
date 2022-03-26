package com.example.passwordmanager.ui.home

import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordmanager.R
import com.example.passwordmanager.dao.PassInfoDao
import com.example.passwordmanager.entity.PasswordInfo
import com.example.passwordmanager.ui.edit_password.EditPasswordFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.schedulers.Schedulers
import org.koin.java.KoinJavaComponent.inject

class HomeViewHolder(item: View): RecyclerView.ViewHolder(item) {
    companion object {
        const val RESULT_KEY = "password_id"
    }

    private val dao by inject<PassInfoDao>(PassInfoDao::class.java)
    private val inputMethod by inject<InputMethodManager>(InputMethodManager::class.java)

    val foreground: View = item.findViewById(R.id.home_item_foreground)
    val background: View = item.findViewById(R.id.home_item_background)

    val itemSiteName: TextView        = item.findViewById(R.id.home_item_site_name)
    val itemUrl: TextView             = item.findViewById(R.id.home_item_url)
    val itemDeleteButton: ImageButton = item.findViewById(R.id.home_item_delete_button)

    /** パスワード編集画面表示 */
    fun showEditPassword(manager: FragmentManager, id: Int) {
        val same = (manager.findFragmentByTag(HomeFragment.TAG_EDIT_PASSWORD) as? BottomSheetDialogFragment)?.dialog
        if (same == null) { // 二重表示防止
            foreground.rootView.requestFocus() //フォーカスをrootに当てる
            inputMethod.hideSoftInputFromWindow(foreground.windowToken, 0)

            // キーと値を渡してEditPasswordFragmentとフラグメント間通信
            val args = bundleOf(RESULT_KEY to id)
            manager.setFragmentResult(EditPasswordFragment::class.java.name, args)

            EditPasswordFragment().showNow(manager, HomeFragment.TAG_EDIT_PASSWORD)
        }
    }

    /** パスワード削除 */
    fun deletePassword(id: Int) {
        dao
            .delete(PasswordInfo.default(id))
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}