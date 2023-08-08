package com.thirfir.domain.model

data class TextElement(
    val text: String,
    val style: MutableMap<String, String> = mutableMapOf(),

)