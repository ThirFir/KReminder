package com.thirfir.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.thirfir.data.datasource.local.entitiy.KeywordEntity

@Dao
interface KeywordDao {
    @Query("SELECT * FROM keyword")
    fun getAll(): List<KeywordEntity>

    @Insert
    fun insert(keyword: KeywordEntity)

    @Query("DELETE FROM keyword WHERE name = :name")
    fun delete(name: String)

    @Query("DELETE FROM keyword")
    fun deleteAll()
}