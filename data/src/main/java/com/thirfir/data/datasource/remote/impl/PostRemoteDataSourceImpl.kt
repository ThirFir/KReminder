package com.thirfir.data.datasource.remote.impl

import com.thirfir.data.datasource.remote.PostRemoteDataSource
import com.thirfir.data.datasource.remote.dto.PostDTO
import com.thirfir.domain.BASE_URL
import com.thirfir.domain.BOLD
import com.thirfir.domain.BOLD_TAG
import com.thirfir.domain.FONT_WEIGHT
import com.thirfir.domain.H3_TAG
import com.thirfir.domain.ITALIC
import com.thirfir.domain.ITALIC_TAG
import com.thirfir.domain.LINE_THROUGH
import com.thirfir.domain.P_TAG
import com.thirfir.domain.STRIKE_TAG
import com.thirfir.domain.STYLE
import com.thirfir.domain.TABLE_TAG
import com.thirfir.domain.TEXT_DECORATION_LINE
import com.thirfir.domain.UNDERLINE
import com.thirfir.domain.UNDERLINE_TAG
import com.thirfir.domain.addQueryString
import com.thirfir.domain.model.element.EnabledRootTag
import com.thirfir.domain.model.element.ParentElement
import com.thirfir.domain.model.element.TableElement
import com.thirfir.domain.model.element.TextElement
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class PostRemoteDataSourceImpl : PostRemoteDataSource {

    private val parentElements: MutableList<ParentElement> = mutableListOf()
    override suspend fun getPostDTO(bulletin: Int, pid: Int): PostDTO {
        val doc =
            Jsoup.connect(
                BASE_URL.addQueryString("b", bulletin)
                    .addQueryString("p", pid)).get()
        val mParentElements = doc.select(".bc-s-post-ctnt-area > *")

        mParentElements.forEachIndexed { index, element ->
            if (index >= parentElements.size) {
                parentElements.add(
                    ParentElement(
                    mutableListOf(),
                    EnabledRootTag.P,
                    mutableListOf()
                )
                )
            }
            if(element.tagName() == P_TAG) {
                parentElements[index].enabledRootTag = EnabledRootTag.P
                extractTextElements(element, index, Decorations(), extractStyles(element.attr(STYLE)))
            }
            else if(element.tagName() == TABLE_TAG) {
                parentElements[index].enabledRootTag = EnabledRootTag.TABLE
                extractTable(element.select("tbody")[0], index)
            }
            else if(element.tagName() == H3_TAG)
                parentElements[index].enabledRootTag = EnabledRootTag.H3

        }

        return PostDTO(parentElements)
    }

    /**
     * @param element 현재 element
     * @param index 최상위 태그 index
     * @param u 현재 element가 underline인지
     * @param b 현재 element가 bold인지
     */
    private fun extractTextElements(element: Element, index: Int, parentDeco: Decorations, parentStyles: Map<String, String>) {


        // 모든 자식 element 순환
        element.children().forEach {
            var deco = parentDeco

            if (it.tagName().trim() == UNDERLINE_TAG)
                deco.underline = true
            else if (it.tagName().trim() == BOLD_TAG)
                deco.bold = true
            else if (it.tagName().trim() == STRIKE_TAG)
                deco.strike = true
            else if (it.tagName().trim() == ITALIC_TAG)
                deco.italic = true

            val styles = extractStyles(it.attr(STYLE)).apply {
                // 자식 뷰에 부모 스타일 적용
                parentStyles.forEach { parentStyle ->
                    if(this[parentStyle.key] == null)
                        this[parentStyle.key] = parentStyle.value
                }
            }
            parentElements[index].textElements.add(TextElement(it.ownText(), styles))
            if (deco.underline)
                parentElements[index].textElements[parentElements[index].textElements.lastIndex].style[TEXT_DECORATION_LINE] =
                    UNDERLINE
            if (deco.bold)
                parentElements[index].textElements[parentElements[index].textElements.lastIndex].style[FONT_WEIGHT] = BOLD
            if (deco.strike)
                parentElements[index].textElements[parentElements[index].textElements.lastIndex].style[TEXT_DECORATION_LINE] =
                    LINE_THROUGH
            if (deco.italic)
                parentElements[index].textElements[parentElements[index].textElements.lastIndex].style[FONT_WEIGHT] = ITALIC

            extractTextElements(it, index, deco, styles)
        }
    }

    private fun extractTable(tbody: Element, index: Int) {
        // 테이블 사이즈 구하기 및 초기화
        val rowSize = getTableHeight(tbody)
        val colSize = getTableWidth(tbody)
        for (i in 0 until rowSize) {
            for (j in 0 until colSize) {
                parentElements[index].tables = MutableList(rowSize) { MutableList(colSize) { TableElement(
                    mutableListOf(),
                    1,
                    1
                ) } }
            }
        }

        tbody.children().forEachIndexed { trIndex, tr ->
            tr.children().forEachIndexed { tdIndex, td ->
                if (td.tagName() == "td") {
                    if(parentElements[index].tables!![trIndex][tdIndex] == null)
                        return@forEachIndexed
                    var rowSpan = 1
                    if(td.attr("rowspan") != "")
                        rowSpan = td.attr("rowspan").toInt()
                    var colSpan = 1
                    if(td.attr("colspan") != "")
                        colSpan = td.attr("colspan").toInt()

                    for(rowIndex in 1 until rowSpan)
                        parentElements[index].tables!![trIndex + rowIndex][tdIndex] = null  // rowspan 만큼 아래 칸 무효화
                    for(columnIndex in 1 until colSpan)
                        parentElements[index].tables!![trIndex][tdIndex + columnIndex] = null  // colspan 만큼 오른쪽 칸 무효화
                    extractTdTextElements(td, index, trIndex, tdIndex)
                }
            }
        }

    }

    private fun extractTdTextElements(e: Element, index: Int, rowIndex: Int, colIndex: Int) {
        e.children().forEach {
            parentElements[index].tables!![rowIndex][colIndex]!!.textElement
                .add(TextElement(it.ownText(), extractStyles(it.attr("style"))))

            extractTdTextElements(it, index, rowIndex, colIndex)
        }
    }

    private fun getTableHeight(tbody: Element): Int {
        return tbody.children().size
    }
    private fun getTableWidth(tbody: Element): Int {
        var maxWidth = 0
        tbody.children().forEach {
            maxWidth = maxWidth.coerceAtLeast(it.children().size)
        }
        return maxWidth
    }

    private fun extractStyles(styleAttr: String): MutableMap<String, String> {
        val styleAttrs = styleAttr.split(";")
        val styles = mutableMapOf<String, String>()
        for (style in styleAttrs) {
            val parts = style.trim().split(":")

            if (parts.size == 2) {
                styles[parts[0].trim().lowercase()] = parts[1].trim().lowercase()
            }
        }
        return styles
    }

    data class Decorations(
        var underline: Boolean = false,
        var bold: Boolean = false,
        var italic: Boolean = false,
        var strike: Boolean = false,
    )
}



