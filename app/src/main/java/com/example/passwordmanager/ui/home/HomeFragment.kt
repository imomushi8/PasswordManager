package com.example.passwordmanager.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passwordmanager.R
import com.example.passwordmanager.base.viewBinding
import com.example.passwordmanager.databinding.FragmentHomeBinding

/*
TODO: 1. CSVファイルから、パスワードデータ入力できるようにする(３点リーダーみたいなやつから遷移できるようにしたいなぁ)　また、逆にCSVファイル化できるようにしたい
TODO: 2. generatorのchipの色が気色悪いので直す。というか全体的に配色を直すのはアリ（上記と関係する）
TODO: 3. ダークモード使用時のCardView?の背景色を直してみる（というかダークモード時の全体的なスタイル変更が必要な気がする）
TODO: 4. HomeViewHolderのスワイプアニメーションとか、直せたら直したい。
*/

class HomeFragment: Fragment(R.layout.fragment_home) {
    companion object {
        /** パスワード詳細画面のタグ */
        const val TAG_SHOW_PASSWORD = "ShowPassword"
    }

    private val viewModel by viewModels<HomeViewModel>()
    private val _binding  by viewBinding(FragmentHomeBinding::bind)
    private val  binding get() = checkNotNull(_binding)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLManager          = LinearLayoutManager(context) // RecyclerViewに使用する
        val itemTouchHelperCallback = HomeItemTouchHelperCallback.newInstance()
        val homeRecyclerAdapter     = HomeRecyclerAdapter(itemTouchHelperCallback, childFragmentManager)

        binding.also {
            // リストの変更を通知
            viewModel.notifyDBChange(viewLifecycleOwner, homeRecyclerAdapter)

            // RecyclerViewの設定
            it.passwordRecyclerView.run {
                adapter = homeRecyclerAdapter
                layoutManager = linearLManager
                addItemDecoration(DividerItemDecoration(context, linearLManager.orientation))
                itemTouchHelperCallback.itemTouchHelper.attachToRecyclerView(this)
            }

            // 検索バーの設定
            it.homeSearchBar.run {
                setOnQueryTextFocusChangeListener(viewModel::searchFocused)
                setOnQueryTextListener(viewModel.search(this, homeRecyclerAdapter))
            }

            // パスワード追加FABのイベント
            it.addPasswordFab.setOnClickListener(viewModel.register(findNavController()))
        }
    }

    /** 画面遷移でフラグメントが中断したとき等に行う */
    override fun onResume() {
        super.onResume()
        // 遷移前の検索ワードを保存しとく
        binding.homeSearchBar.post{ binding.homeSearchBar.setQuery(viewModel.searchQuery, true) }
    }
}