package com.example.passwordmanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.passwordmanager.ui.home.HomePasswordInfo
import com.example.passwordmanager.entity.PasswordInfo
import io.reactivex.Completable
import io.reactivex.Single

/** PasswordInfo用のDAO */
@Dao
interface PassInfoDao {
    @Query("SELECT * FROM password_info ORDER BY title")
    fun getAll(): LiveData<List<PasswordInfo>>

    @Query("SELECT id, title, url FROM password_info ORDER BY title")
    fun getAllHomePassInfo(): LiveData<List<HomePasswordInfo>>

    @Query("SELECT * FROM password_info WHERE title LIKE :query OR url LIKE :query ORDER BY title")
    fun search(query: String): LiveData<List<PasswordInfo>>

    @Query("SELECT * FROM password_info WHERE id = :id")
    fun find(id: Int): Single<PasswordInfo>

    @Query("SELECT * FROM password_info WHERE title IN (:title)")
    fun findByName(title: String): List<PasswordInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(info: PasswordInfo): Completable

    @Delete
    fun delete(info: PasswordInfo): Completable
}