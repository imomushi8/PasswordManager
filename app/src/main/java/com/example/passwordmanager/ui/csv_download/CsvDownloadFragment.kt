package com.example.passwordmanager.ui.csv_download

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.passwordmanager.R
import com.example.passwordmanager.base.viewBinding
import com.example.passwordmanager.databinding.FragmentCsvDownloadBinding

class CsvDownloadFragment: Fragment(R.layout.fragment_csv_download) {
    private val viewModel by viewModels<CsvDownloadViewModel>()
    private val _binding  by viewBinding(FragmentCsvDownloadBinding::bind)
    private val  binding get() = checkNotNull(_binding)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            downloadButton.setOnClickListener(viewModel.downloadOnClickListener(this@CsvDownloadFragment))
        }
    }

}