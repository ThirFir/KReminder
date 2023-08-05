package com.thirfir.domain.model

// 글내용, 이미지, 첨부 파일 TODO : 텍스트 색상, 표, ... 처리 어떻게 할지
data class Post(
    val textElements: List<List<TextElement>>,
    // TODO : 데이터 정의
)