package com.example.passwordmanager.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passwordmanager.R
import com.example.passwordmanager.base.viewBinding
import com.example.passwordmanager.databinding.FragmentHomeBinding

class HomeFragment: Fragment(R.layout.fragment_home) {
    companion object {
        /** パスワード編集画面のタグ */
        const val TAG_EDIT_PASSWORD = "EditPassword"
    }

    private val viewModel by viewModels<HomeViewModel>()
    private val _binding  by viewBinding(FragmentHomeBinding::bind)
    private val  binding get() = checkNotNull(_binding)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeRecyclerAdapter = HomeRecyclerAdapter(childFragmentManager)
        val linearLManager     = LinearLayoutManager(context) // RecyclerViewに使用する
        val itemTouchHelper    = viewModel.passItemTouchHelperCallback(homeRecyclerAdapter).itemTouchHelper

        // リストの変更を通知
        viewModel.notifyDBChange(viewLifecycleOwner, homeRecyclerAdapter)

        binding.run {
            // RecyclerViewの設定
            passwordRecyclerView.run {
                adapter = homeRecyclerAdapter
                layoutManager = linearLManager
                addItemDecoration(DividerItemDecoration(context, linearLManager.orientation))
                itemTouchHelper.attachToRecyclerView(this)
            }

            // 検索バーの設定
            homeSearchBar.apply {
                setOnQueryTextFocusChangeListener(viewModel::searchFocused)
                setOnQueryTextListener(viewModel.searchQueryListener(this, homeRecyclerAdapter))
            }

            // パスワード追加FABのイベント
            addPasswordFab.setOnClickListener(viewModel.registerFABListener(childFragmentManager))
        }
    }
}