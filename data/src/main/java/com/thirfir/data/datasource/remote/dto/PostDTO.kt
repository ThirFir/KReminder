package com.thirfir.data.datasource.remote.dto

import com.thirfir.domain.model.element.ParentElement

data class PostDTO(
    val parentElements: List<ParentElement>
)