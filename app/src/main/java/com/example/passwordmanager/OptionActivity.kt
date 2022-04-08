package com.example.passwordmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.passwordmanager.base.viewBinding
import com.example.passwordmanager.databinding.ActivityOptionBinding

class OptionActivity: AppCompatActivity(R.layout.activity_option) {
    private val binding: ActivityOptionBinding by viewBinding(ActivityOptionBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController = findNavController(R.id.option_nav_host)

        val intentId = intent.getIntExtra(MainActivity.ITEM_ID, -1)
        if(intentId >= 0) navController.navigate(intentId) // intentIdが取得できればnavigation

        binding.run {
            optionToolbar.setupWithNavController(navController)
        }
    }
}