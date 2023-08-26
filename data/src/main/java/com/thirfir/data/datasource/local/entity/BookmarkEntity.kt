package com.thirfir.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark")
data class BookmarkEntity(
    val bulletin: Int,
    @PrimaryKey val pid: Int,
    val title: String,
    val category: String,
    val timestamp: Long,
)