package com.example.passwordmanager.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** パスワード情報をすべて保持しているBean */
@Entity(tableName = "password_info")
data class PasswordInfo(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "url") var url: String?,
    @ColumnInfo(name = "user_id") var user_id: String?,
    @ColumnInfo(name = "user_name") var user_name: String?,
    @ColumnInfo(name = "e_mail") var e_mail: String?,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "number_password") var number_password: String?,
    @ColumnInfo(name = "secret_question1") var secret_question1: String?,
    @ColumnInfo(name = "secret_answer1") var secret_answer1: String?,
    @ColumnInfo(name = "secret_question2") var secret_question2: String?,
    @ColumnInfo(name = "secret_answer2") var secret_answer2: String?,
    @ColumnInfo(name = "secret_question3") var secret_question3: String?,
    @ColumnInfo(name = "secret_answer3") var secret_answer3: String?,
    @ColumnInfo(name = "memo") var memo: String?
) {
    companion object {
        /** デフォルト値を生成する
         *  ※変数にすると、シングルトンインスタンスを利用していることになって予期せぬエラーとなるので、関数としている
         * */
        fun default(id: Int = 0) = PasswordInfo(
            id,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "")

        val csvHeader = listOf(listOf(
            "title",
            "url",
            "user_id",
            "user_name",
            "e_mail",
            "password",
            "number_password",
            "secret_question1",
            "secret_answer1",
            "secret_question2",
            "secret_answer2",
            "secret_question3",
            "secret_answer3",
            "memo"
            ))

        fun toCsv(passInfo: PasswordInfo): List<String> = listOf(
            passInfo.title,
            passInfo.url ?: "",
            passInfo.user_id ?: "",
            passInfo.user_name ?: "",
            passInfo.e_mail ?: "",
            passInfo.password,
            passInfo.number_password ?: "",
            passInfo.secret_question1 ?: "",
            passInfo.secret_answer1 ?: "",
            passInfo.secret_question2 ?: "",
            passInfo.secret_answer2 ?: "",
            passInfo.secret_question3 ?: "",
            passInfo.secret_answer3 ?: "",
            passInfo.memo ?: ""
        )
    }
}