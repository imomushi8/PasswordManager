package com.example.passwordmanager.data_entity

import androidx.room.ColumnInfo
import java.util.*

data class HomePasswordInfo(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "url") val url: String?
    ) {
    fun search(searchString: String): Boolean =
        title.lowercase(Locale.ROOT).contains(searchString) ||
        url?.lowercase(Locale.ROOT)?.contains(searchString) == true
}
