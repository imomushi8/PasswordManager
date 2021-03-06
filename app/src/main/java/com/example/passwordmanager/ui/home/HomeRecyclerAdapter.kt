package com.example.passwordmanager.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordmanager.R

class HomeRecyclerAdapter(private val itemTouchHelperCallback: HomeItemTouchHelperCallback,
                          private val childFragmentManager: FragmentManager): RecyclerView.Adapter<HomeViewHolder>() {

    var searchResults: List<HomePasswordInfo> = listOf() // 全情報を所持しているリスト

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_home_recycle, parent, false)
        return HomeViewHolder(itemView)
    }

    /** ここでViewHolderに（つまりアイテム１つ１つに対して）イベント定義等を行う */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) = holder.run {
        // holderをスワイプされていない状態にする
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(background)
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foreground)

        val passInfo = searchResults[position]

        itemSiteName.text = passInfo.title
        itemUrl.text = "URL: ${ passInfo.url ?: "" }" // nullなら空文字にする

        // 削除ボタンをタッチしたときのイベント定義
        itemDeleteButton.setOnClickListener {
            deletePassword(passInfo.id, itemTouchHelperCallback)
        }

        // itemをタッチしたときのイベント定義
        foreground.setOnClickListener { showDetailPassword(childFragmentManager, passInfo.id) }
    }

    override fun getItemCount(): Int = searchResults.size
}