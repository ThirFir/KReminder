package com.thirfir.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.thirfir.data.datasource.local.entity.BookmarkEntity

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmark")
    fun getAll(): List<BookmarkEntity>

    @Insert
    fun insert(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmark WHERE pid = :pid")
    fun delete(pid: Int)
}