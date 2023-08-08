package com.thirfir.domain.model.element

data class TextElement(
    val text: String,
    val style: MutableMap<String, String> = mutableMapOf(),
)