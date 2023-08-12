package com.thirfir.data.datasource.remote.dto

data class HtmlElementDTO(
    val tag: String?,
    val text: String?,
    val attributes: MutableMap<String, String> = mutableMapOf(),
    val childElement: MutableList<HtmlElementDTO>?,
)