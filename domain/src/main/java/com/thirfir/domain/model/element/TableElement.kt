package com.thirfir.domain.model.element

data class TableElement(
    val textElement: MutableList<TextElement>,
    var rowSpan: Int = 1,
    var colSpan: Int = 1
)