package com.thirfir.data.datasource.local.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keyword")
data class KeywordEntity(
    @PrimaryKey val name: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)
