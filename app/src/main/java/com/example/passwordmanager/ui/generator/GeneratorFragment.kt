package com.example.passwordmanager.ui.generator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.passwordmanager.R
import com.example.passwordmanager.base.ValidType
import com.example.passwordmanager.base.Validator
import com.example.passwordmanager.base.viewBinding
import com.example.passwordmanager.databinding.FragmentGeneratorBinding

class GeneratorFragment : Fragment(R.layout.fragment_generator) {
    private val viewModel by viewModels<GeneratorViewModel>()
    private val _binding  by viewBinding(FragmentGeneratorBinding::bind)
    private val  binding get() = checkNotNull(_binding)

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val generatorRecyclerAdapter = GeneratorRecyclerAdapter()
        val gridLManager = GridLayoutManager(context, 2) // RecyclerViewに使用する

        binding.run {
            // Chipを生成
            viewModel.setChips(generatorChipGroup, requireContext())
            viewModel.notifyCreatePasswords(viewLifecycleOwner, generatorRecyclerAdapter)

            generatorButton.setOnClickListener {
                val isOk = Validator.checkAllEditText(
                    generatePassLengthInput to listOf(ValidType.Require),
                    generatePassSizeInput to listOf(ValidType.Require)
                )

                if(isOk) {
                    viewModel.generate(
                        generatePassLengthInput.text.toString().toInt(),
                        generatePassSizeInput.text.toString().toInt())
                }
            }

            // RecyclerAdapterの設定
            generatePasswordRecyclerView.run {
                adapter = generatorRecyclerAdapter
                layoutManager = gridLManager
                addItemDecoration(DividerItemDecoration(context, gridLManager.orientation))
            }
        }
    }

}