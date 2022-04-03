package com.example.passwordmanager.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.passwordmanager.ui.home.HomePasswordInfo

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
    }
}