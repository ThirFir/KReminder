package com.thirfir.domain.model

import com.thirfir.domain.model.element.ParentElement

data class Post(
    val parentElements: List<ParentElement>,
)