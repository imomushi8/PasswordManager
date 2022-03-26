package com.example.passwordmanager.ui.home

import android.annotation.SuppressLint
import android.app.Application
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.passwordmanager.dao.PassInfoDao
import com.example.passwordmanager.data_entity.HomePasswordInfo
import com.example.passwordmanager.ui.edit_password.EditPasswordFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.java.KoinJavaComponent.inject
import java.util.*

/** ホーム画面のViewModel */
class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val dao         by inject<PassInfoDao>(PassInfoDao::class.java)
    private val inputMethod by inject<InputMethodManager>(InputMethodManager::class.java)

    /** パスワードリストのViewバインディング用変数 */
    private val livePassList: LiveData<List<HomePasswordInfo>> = dao.getAllHomePassInfo() // 表示用のデータ(LiveData)

    fun passItemTouchHelperCallback(adapter: HomeRecyclerAdapter) = HomeItemTouchHelper.newInstance(adapter)

    /** パスワード登録のFABの動作イベント */
    fun registerFABListener(childFragmentManager: FragmentManager) = View.OnClickListener {
        // 連打対策で、同じタグ名のフラグメントが存在していない場合に開くようにする
        val same = (childFragmentManager.findFragmentByTag(HomeFragment.TAG_EDIT_PASSWORD) as? BottomSheetDialogFragment)?.dialog
        if (same == null) {
            it.rootView.requestFocus() //フォーカスをrootに当てる
            inputMethod.hideSoftInputFromWindow(it.windowToken, 0)
            EditPasswordFragment().showNow(childFragmentManager, HomeFragment.TAG_EDIT_PASSWORD)
        }
    }

    /** 検索時の動作イベント */
    fun searchQueryListener(view: View, adapter: HomeRecyclerAdapter) = object: SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String?): Boolean = filterPassList(adapter, newText)
        override fun onQueryTextSubmit(query: String?): Boolean {
            view.rootView.requestFocus() //フォーカスをrootに当てる
            inputMethod.hideSoftInputFromWindow(view.windowToken, 0) // submitでキーボードを閉じる
            return filterPassList(adapter, query)
        }
    }

    /** 検索バーにフォーカスがあてられたときの動作 */
    fun searchFocused(view: View, hasFocus: Boolean) {
        if (!hasFocus) inputMethod.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /** DBの変更をRecyclerViewに通知する */
    @SuppressLint("NotifyDataSetChanged")
    fun notifyDBChange(viewLifecycleOwner: LifecycleOwner, adapter: HomeRecyclerAdapter) {
        livePassList.observe(viewLifecycleOwner) { sqlResults ->
            adapter
                .apply { searchResults = sqlResults }
                .notifyDataSetChanged() // 変更を通知
        }
    }

    /** 検索結果の変更をRecyclerViewに通知する
     * TODO: OR検索できるようにする
     * */
    @SuppressLint("NotifyDataSetChanged")
    private fun filterPassList(adapter: HomeRecyclerAdapter, query: String?): Boolean {
        val searchString = (query ?: "").trim().lowercase(Locale.ROOT)
        adapter
            .apply {
                searchResults = livePassList.value?.let { passList -> // getAllしたものをプログラミング言語側でフィルタリングする
                    if (searchString.isEmpty()) passList
                    else passList.filter { it.search(searchString) }
                } ?: listOf()
            }
            .notifyDataSetChanged()
        return true
    }
}