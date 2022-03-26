package com.example.passwordmanager.base

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import com.example.passwordmanager.databinding.FragmentEditPasswordBinding
import com.example.passwordmanager.ui.edit_password.EditPasswordViewModel

interface IViewModel {
    
    /** ViewModelはライフサイクルが長いので、予想外のデータが残らないように初期化用のメソッドは作ったほうが良い */
    fun initialize()
}