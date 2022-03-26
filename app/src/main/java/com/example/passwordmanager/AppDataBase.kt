package com.example.passwordmanager

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.passwordmanager.dao.PassInfoDao
import com.example.passwordmanager.entity.PasswordInfo

/** アプリのデータベース */
@Database(entities = [PasswordInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun passInfoDao(): PassInfoDao

}