package com.example.passwordmanager.ui.edit_password

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.example.passwordmanager.R
import com.example.passwordmanager.base.ValidType
import com.example.passwordmanager.base.Validator
import com.example.passwordmanager.base.dataBinding
import com.example.passwordmanager.databinding.FragmentEditPasswordBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/** パスワード情報編集フラグメント(BottomSheet) */
class EditPasswordFragment: BottomSheetDialogFragment() {
    private val viewModel by viewModels<EditPasswordViewModel>()
    private val _binding  by dataBinding<FragmentEditPasswordBinding>()
    private val  binding get() = checkNotNull(_binding)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_edit_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 親Fragmentからのリクエストを受け取る
        setFragmentResultListener(javaClass.name) { requestKey, bundle ->
            if (requestKey != javaClass.name) return@setFragmentResultListener
            viewModel.getResult(bundle)
        }

        // イベント定義
        binding.run {
            vm = viewModel
            //lifecycleOwner = viewLifecycleOwner // fragmentを使うときに渡すlifecycleはこれ

            // トグルスイッチの設定
            userIdSwitch  .setOnCheckedChangeListener { _, isUseFlag -> viewModel.switchUserIdFlag(userIdInput, isUseFlag) }
            userNameSwitch.setOnCheckedChangeListener { _, isUseFlag -> viewModel.switchUserNameFlag(userNameInput, isUseFlag) }

            // コピペボタンの挙動 空文字をコピーしないようなってる（かしこい）
            urlCopyButton           .setOnClickListener { viewModel.copyText("url", urlInput) }
            userIdCopyButton        .setOnClickListener { viewModel.copyText("user_id", userIdInput) }
            userNameCopyButton      .setOnClickListener { viewModel.copyText("user_name", userNameInput) }
            emailCopyButton         .setOnClickListener { viewModel.copyText("e_mail", emailInput) }
            passwordCopyButton      .setOnClickListener { viewModel.copyText("password", passwordInput) }
            numberPasswordCopyButton.setOnClickListener { viewModel.copyText("number_password", numberPasswordInput) }
            secretAnswer1CopyButton .setOnClickListener { viewModel.copyText("sec_ans1", secretAnswer1Input) }
            secretAnswer2CopyButton .setOnClickListener { viewModel.copyText("sec_ans2", secretAnswer2Input) }
            secretAnswer3CopyButton .setOnClickListener { viewModel.copyText("sec_ans3", secretAnswer3Input) }

            // 登録ボタン押下時のイベント
            registerButton.setOnClickListener { button ->
                button.isEnabled = false // 二重押下防止のための非活性化

                // バリデータ定義
                val isOk = Validator.checkAllEditText(
                    titleInput to listOf(ValidType.Require),
                    passwordInput to listOf(ValidType.Require)
                )
                if(isOk) {
                    viewModel.register()
                    dismissAllowingStateLoss() // このフラグメントを閉じる（データは残らない）
                } else
                    button.isEnabled = true
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.initialize()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.initialize()
    }
}