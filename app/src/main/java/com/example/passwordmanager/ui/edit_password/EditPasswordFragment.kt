package com.example.passwordmanager.ui.edit_password

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.passwordmanager.R
import com.example.passwordmanager.base.ValidType
import com.example.passwordmanager.base.Validator
import com.example.passwordmanager.base.dataBinding
import com.example.passwordmanager.databinding.FragmentEditPasswordBinding

class EditPasswordFragment: Fragment(R.layout.fragment_edit_password) {
    private val viewModel by viewModels<EditPasswordViewModel>()
    private val _binding  by dataBinding<FragmentEditPasswordBinding>()
    private val  binding get() = checkNotNull(_binding)

    /** SafeArgsで受け取った引数 */
    private val args: EditPasswordFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPassInfo(args)

        val navController = findNavController()

        // イベント定義
        binding.run {
            vm = viewModel

            // トグルスイッチの設定
            userIdSwitch  .setOnCheckedChangeListener { _, isUseFlag -> viewModel.switchUserIdFlag(userIdInput, isUseFlag) }
            userNameSwitch.setOnCheckedChangeListener { _, isUseFlag -> viewModel.switchUserNameFlag(userNameInput, isUseFlag) }

            // 登録ボタン押下時のイベント
            registerButton.setOnClickListener { button ->
                button.isEnabled = false // 二重押下防止のための非活性化

                // バリデータ定義
                val isOk = Validator.checkAllEditText(
                    titleInput to listOf(ValidType.Require),
                    passwordInput to listOf(ValidType.Require)
                )
                if(isOk) {
                    viewModel.register(navController)
                } else
                    button.isEnabled = true
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, viewModel.backKeyPressedCallback(navController))
    }
}