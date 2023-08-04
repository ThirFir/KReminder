package com.thirfir.domain.model


data class PostHeader(
    val pid: Int,
    val title: String,
    val author: String,
    val date: String,
    val isTopFixed: Boolean,
    // TODO : 데이터 정의
)
