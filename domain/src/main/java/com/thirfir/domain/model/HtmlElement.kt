package com.thirfir.domain.model

data class HtmlElement (
    val tag: String?,
    val text: String?,
    val attributes: MutableMap<String, String> = mutableMapOf(),
    val childElements: MutableList<HtmlElement>?,
    val styles: MutableMap<String, String>
)