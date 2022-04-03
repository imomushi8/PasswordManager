package com.example.passwordmanager.ui.detail_password

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.passwordmanager.R
import com.example.passwordmanager.base.dataBinding
import com.example.passwordmanager.databinding.FragmentDetailPasswordBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/** パスワード情報編集フラグメント(BottomSheet) */
class DetailPasswordFragment: BottomSheetDialogFragment() {
    private val viewModel by viewModels<DetailPasswordViewModel>()
    private val _binding  by dataBinding<FragmentDetailPasswordBinding>()
    private val  binding get() = checkNotNull(_binding)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_detail_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 親Fragmentからのリクエストを受け取る
        setFragmentResultListener(javaClass.name) { requestKey, bundle ->
            if (requestKey != javaClass.name) return@setFragmentResultListener
            viewModel.getResult(bundle)
        }

        // イベント定義
        binding.also { bind ->
            bind.vm = viewModel

            // コピペボタンの挙動 空文字をコピーしないようなってる（かしこい）
            bind.urlCopyButton           .setOnClickListener { viewModel.copyText(bind.urlContent) }
            bind.userIdCopyButton        .setOnClickListener { viewModel.copyText(bind.userIdContent) }
            bind.userNameCopyButton      .setOnClickListener { viewModel.copyText(bind.userNameContent) }
            bind.emailCopyButton         .setOnClickListener { viewModel.copyText(bind.emailContent) }
            bind.passwordCopyButton      .setOnClickListener { viewModel.copyText(bind.passwordContent) }
            bind.numberPasswordCopyButton.setOnClickListener { viewModel.copyText(bind.numberPasswordContent) }
            bind.secretAnswer1CopyButton .setOnClickListener { viewModel.copyText(bind.secretAnswer1Content) }
            bind.secretAnswer2CopyButton .setOnClickListener { viewModel.copyText(bind.secretAnswer2Content) }
            bind.secretAnswer3CopyButton .setOnClickListener { viewModel.copyText(bind.secretAnswer3Content) }
            bind.memoCopyButton          .setOnClickListener { viewModel.copyText(bind.memoContent) }

            // 編集ボタン押下時のイベント
            bind.editButton.setOnClickListener(viewModel.edit(this, findNavController()))
        }
    }
}