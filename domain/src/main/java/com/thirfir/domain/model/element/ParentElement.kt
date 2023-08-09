package com.thirfir.domain.model.element


data class ParentElement(   // 이거의 list
    var textElements: MutableList<TextElement>,
    var enabledRootTag: EnabledRootTag,
    var tables: MutableList<MutableList<TableElement?>>? = null
)

/*
parentElement[0] = <p>...</p>
parentElement[1] = <p>...</p>
parentElement[2] = <table>...</table>

parentElement[0].textElement[0].text = "내용"
parentElement[0].textElement[0].style[FONT_WEIGHT] // = "bold"      0번째 RootTag의 0번째 span(or others...)의 style

parentElement[2].tables[0] = <tr>...</tr>
parentElement[2].tables[1] = <tr>...</tr>

parentElement[2].table[0][0] = <td>...</td>
parentElement[2].table[0][0].textElement.text = "내용"
parentElement[2].table[0][0].textElement.style[BACKGROUND] // = "red"
 */
