package com.example.passwordmanager.ui.home

import android.annotation.SuppressLint
import android.app.Application
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.passwordmanager.R
import com.example.passwordmanager.dao.PassInfoDao
import org.koin.java.KoinJavaComponent.inject
import java.util.*


/** ホーム画面のViewModel */
class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val dao         by inject<PassInfoDao>(PassInfoDao::class.java)
    private val inputMethod by inject<InputMethodManager>(InputMethodManager::class.java)

    /** パスワードリストのViewバインディング用変数 */
    private val livePassList: LiveData<List<HomePasswordInfo>> = dao.getAllHomePassInfo() // 表示用のデータ(LiveData)

    /** SearchViewのクエリを保存しておく変数 */
    var searchQuery = ""

    /** パスワード登録のFABの動作イベント */
    fun register(navController: NavController) = View.OnClickListener {
        it.isEnabled = false
        navController.navigate(R.id.action_navigation_home_to_navigation_edit_password)
        it.isEnabled = true
    }

    /** 検索時の動作イベント */
    fun search(view: View, adapter: HomeRecyclerAdapter) = object: SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String?): Boolean {
            searchQuery = (newText ?: "")
            notifyAdapter(adapter,livePassList.value ?: listOf())
            return true
        }

        override fun onQueryTextSubmit(query: String?): Boolean {
            searchQuery = (query ?: "")
            view.rootView.requestFocus() //フォーカスをrootに当てる setOnQueryTextFocusChangeListenerでキーボードを隠すようにしてるので、それで動く
            notifyAdapter(adapter,livePassList.value ?: listOf())
            return true
        }
    }

    /** 検索バーにフォーカスがあてられたときの動作 */
    fun searchFocused(view: View, hasFocus: Boolean) {
        if (!hasFocus) inputMethod.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /** DBの変更をRecyclerViewに通知する */
    @SuppressLint("NotifyDataSetChanged")
    fun notifyDBChange(viewLifecycleOwner: LifecycleOwner, adapter: HomeRecyclerAdapter) {
        livePassList.observe(viewLifecycleOwner) { notifyAdapter(adapter,it) }
    }

    /** searchQueryでpasswordsをフィルタリングしadapterにバインドする */
    @SuppressLint("NotifyDataSetChanged")
    private fun notifyAdapter(adapter: HomeRecyclerAdapter, passwordList: List<HomePasswordInfo>) {
        adapter
            .apply { searchResults = filterPasswords(passwordList) }
            .notifyDataSetChanged()
    }

    /** searchQueryを使ってpasswordListをフィルタリングする
     * TODO: OR検索できるようにする
     * */
    private fun filterPasswords(passwordList: List<HomePasswordInfo>): List<HomePasswordInfo> {
        val query = searchQuery.trim().lowercase(Locale.ROOT)
        return if (query.isEmpty())
            passwordList
        else
            passwordList.filter { it.search(query) }
    }
}