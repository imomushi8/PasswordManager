package com.example.passwordmanager.ui.generator

import android.content.ClipData
import android.content.ClipboardManager
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordmanager.R
import org.koin.java.KoinJavaComponent.inject

class GeneratorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val clipboard by inject<ClipboardManager>(ClipboardManager::class.java)

    val itemPassword: TextView = itemView.findViewById(R.id.gen_item_pass_text)
    val itemCopyButton: ImageButton = itemView.findViewById(R.id.gen_item_copy_button)

    fun copyPassword(password: String) {
        clipboard.setPrimaryClip(ClipData.newPlainText("generatePassword", password))
    }
}