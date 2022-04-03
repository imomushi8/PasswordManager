package com.example.passwordmanager.ui.generator

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.passwordmanager.R
import com.example.passwordmanager.ui.home.HomeRecyclerAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class GeneratorViewModel(application: Application): AndroidViewModel(application) {
    data class ChipInfo(val title: String, val value: String, var isCheck: Boolean)

    /** パスワード生成に利用する値を保管するリスト */
    private val chipInfoList = mutableListOf(
        ChipInfo("英字(大文字・小文字)", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", true),
        ChipInfo("数字", "1234567890", true),
        ChipInfo("!","!",true),
        ChipInfo("?","?",true),
        ChipInfo("#","#",true),
        ChipInfo("$","$",true),
        ChipInfo("%","%",true),
        ChipInfo("&","&",true),
        ChipInfo("=","=",true),
        ChipInfo("-","-",true),
        ChipInfo("~","~",true),
        ChipInfo("^","^",true),
        ChipInfo("|","|",true),
        ChipInfo("+","+",true),
        ChipInfo("*","*",true),
        ChipInfo("@","@",true),
        ChipInfo("`","`",true),
        ChipInfo(".",".",true),
        ChipInfo(",",",",true),
        ChipInfo("_","_",true),
        ChipInfo(";",";",true),
        ChipInfo(":",":",true),
        ChipInfo("(","(",true),
        ChipInfo(")",")",true),
        ChipInfo("[","[",true),
        ChipInfo("]","]",true),
        ChipInfo("{","{",true),
        ChipInfo("}","}",true)
    )

    /** 生成パスワード用のリスト */
    var liveGeneratedPasswords = MutableLiveData(listOf<String>())

    fun setChips(chipGroup: ChipGroup, context: Context) {
        chipInfoList.forEach{ chipInfo ->
            // layoutparamsは各ViewGroupにあり、設定するViewの存在するGroupと同じものを使う必要がある
            // 今回はChipGroupというViewGroup内にChipというViewを追加するので、ChipGroup.LayoutParamsをchipに追加する
            val params = ChipGroup.LayoutParams(
                ChipGroup.LayoutParams.WRAP_CONTENT,
                ChipGroup.LayoutParams.WRAP_CONTENT
            )

            val chip = Chip(context) // このcontextはGeneratorFragmentのものでなければならない
            chip.isCheckable = true
            chip.checkedIcon = null
            chip.isChecked = chipInfo.isCheck
            chip.text = chipInfo.title
            chip.setChipBackgroundColorResource(if (chipInfo.isCheck) R.color.purple_200 else R.color.silver)
            chip.setOnCheckedChangeListener { _, isChecked ->
                chipInfo.isCheck = isChecked
                chip.setChipBackgroundColorResource(if (isChecked) R.color.purple_200 else R.color.silver)
            }
            chipGroup.addView(chip, params)
        }
    }

    fun generate(genLength: Int, genSize: Int) {
        liveGeneratedPasswords.postValue(generatePasswordList(genLength, genSize))
    }

    /** DBの変更をRecyclerViewに通知する */
    @SuppressLint("NotifyDataSetChanged")
    fun notifyCreatePasswords(viewLifecycleOwner: LifecycleOwner, adapter: GeneratorRecyclerAdapter) {
        liveGeneratedPasswords.observe(viewLifecycleOwner) { generatedPasswords ->
            adapter
                .apply { passwords = generatedPasswords }
                .notifyDataSetChanged() // 変更を通知
        }
    }

    private fun generatePassword(genLength: Int): String {
        val generateSource = chipInfoList
            .filter { it.isCheck }
            .joinToString("") { it.value }

        var rtn = ""

        for(i in 1 .. genLength) {
            val randomChar = generateSource.random()
            rtn += randomChar
        }

        return rtn
    }

    private fun generatePasswordList(genLength: Int, genSize: Int): List<String> {
        return List(genSize) { generatePassword(genLength) }
    }
}