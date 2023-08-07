package com.thirfir.data.datasource.remote.dto

data class PostHeaderDTO(
    val pid: Int,
    val title: String,
    val category: String = "",
    val author: String,
    val date: String,
    val views: String,
    val isTopFixed: Boolean,
)
