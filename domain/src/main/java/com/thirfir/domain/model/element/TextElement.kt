package com.thirfir.domain.model.element

data class TextElement(
    var text: String,
    val style: MutableMap<String, String> = mutableMapOf(),
)