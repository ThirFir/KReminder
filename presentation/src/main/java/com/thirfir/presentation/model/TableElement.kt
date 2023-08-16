package com.thirfir.presentation.model

import com.thirfir.domain.model.HtmlElement

data class TableElement(
    val rowSpan: Int,
    val colSpan: Int,
    val childElements: List<HtmlElement>?,
    val styles: Map<String, String>
)