package com.thirfir.domain.model

data class Bookmark(
    val bulletin: Int,
    val pid: Int,
    val title: String,
    val category: String,
    val timestamp: Long,
)