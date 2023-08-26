package com.thirfir.domain.model

data class Post(
    val htmlElements: List<HtmlElement>,
    val attachedFiles: List<AttachedFile>,
)