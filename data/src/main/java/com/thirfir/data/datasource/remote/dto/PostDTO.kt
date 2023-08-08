package com.thirfir.data.datasource.remote.dto

import com.thirfir.domain.model.ParentElement

data class PostDTO(
    val parentElements: List<ParentElement>
    // TODO : 데이터 정의
    // TODO : 텍스트 색상, 표, ... 처리 어떻게 할지
)