package com.example.passwordmanager.ui.csv_upload

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.passwordmanager.R
import com.example.passwordmanager.base.dataBinding
import com.example.passwordmanager.databinding.FragmentCsvUploadBinding

class CsvUploadFragment: Fragment(R.layout.fragment_csv_upload) {
    private val viewModel by viewModels<CsvUploadViewModel>()
    private val _binding  by dataBinding<FragmentCsvUploadBinding>()
    private val  binding get() = checkNotNull(_binding)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val content = registerForActivityResult(ActivityResultContracts.GetContent()) {
            viewModel.activityResultListener(it, this.requireContext())
        }

        binding.run {
            vm = viewModel

            fileSelectionButton.setOnClickListener(viewModel.fileSelectionClickListener(content))
            uploadButton.setOnClickListener(viewModel.uploadClickListener(requireContext()))
        }
    }

}