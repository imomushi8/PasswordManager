package com.example.passwordmanager.base

import android.widget.EditText

/** バリデーションの種類 */
sealed interface ValidType {
    object Require : ValidType
    data class CheckMin(val min: Int) : ValidType
    data class CheckMax(val max: Int) : ValidType

    fun check(text: String): ValidResult {
        val textLength = text.length
        return when(this) {
            is Require -> if(text.isEmpty()) ValidResult.Empty else ValidResult.Ok
            is CheckMin -> if(min != 0 && textLength >= min) ValidResult.MinOver(min) else ValidResult.Ok
            is CheckMax -> if(max != 0 && textLength <= max) ValidResult.MaxOver(max) else ValidResult.Ok
        }
    }
}

/** バリデーションの結果 */
sealed interface ValidResult {
    object Ok: ValidResult {
        override fun message(name: CharSequence): String = ""
    }
    object Empty: ValidResult {
        override fun message(name: CharSequence): String = "${name}は必須です。"
    }
    data class MinOver(val minLength: Int): ValidResult {
        override fun message(name: CharSequence): String  = "${name}は${minLength}文字以上を入力してください。"
    }
    data class MaxOver(val maxLength: Int): ValidResult {
        override fun message(name: CharSequence): String = "${name}は${maxLength}文字以下を入力してください。"
    }

    fun message(name: CharSequence): String
}


class Validator(
    private val text: String,
    private val name: String,
    private val types: List<ValidType>) {

    companion object {

        /** trueならすべてエラーなし、falseならいずれか1つ以上がエラー */
        fun checkAllEditText(vararg checkList: Pair<EditText, List<ValidType>>): Boolean = checkList
            .map { (editText, types) ->
                val validator = Validator(editText.text.toString(), editText.hint.toString(), types)
                val errors = validator.checkErrors()
                if(errors.isNotEmpty()) editText.error = errors.joinToString(System.lineSeparator())
                return@map errors.isEmpty() // エラーがなければtrue
            }
            .fold(true) { a, b -> a && b }
    }

    /** エラーを確認 Nilならエラーなし */
    fun checkErrors(): List<String> = types
        .map { it.check(text) } // エラーチェック
        .filterNot { it is ValidResult.Ok } // Okの項目のみ除外
        .map { it.message(name) }
}