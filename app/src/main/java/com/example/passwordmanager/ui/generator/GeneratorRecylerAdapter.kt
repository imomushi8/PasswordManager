package com.example.passwordmanager.ui.generator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordmanager.R

class GeneratorRecyclerAdapter: RecyclerView.Adapter<GeneratorViewHolder>() {
    var passwords = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneratorViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_generator_recycle, parent, false)
        return GeneratorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GeneratorViewHolder, position: Int) = holder.run {
        val password = passwords[position]

        itemPassword.text = password
        itemCopyButton.setOnClickListener { copyPassword(password) }
    }

    override fun getItemCount(): Int = passwords.size
}