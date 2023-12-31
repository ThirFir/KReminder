package com.thirfir.data.datasource.local.keyword.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.thirfir.data.datasource.local.keyword.entitiy.KeywordEntity

@Dao
interface KeywordDao {
    @Query("SELECT * FROM keyword")
    fun getAll(): List<KeywordEntity>

    @Insert
    fun insert(keyword: KeywordEntity)

    @Query("DELETE FROM keyword WHERE name = :name")
    fun delete(name: String)
}