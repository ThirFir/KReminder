package com.thirfir.domain.model

// 글내용, 이미지, 첨부 파일 TODO : 텍스트 색상, 표, ... 처리 어떻게 할지
data class Post(
    val header: PostHeader,
    val body: List<String>,
    val file: List<String>? = null // ["/ctt/bb/bulletin?b=14&p=30949&a=fd&fs=1", "..."]
    // TODO : 데이터 정의
)