package com.thirfir.data.datasource.remote.impl

import android.icu.lang.UCharacter.LineBreak.H2
import com.thirfir.data.datasource.remote.PostRemoteDataSource
import com.thirfir.data.datasource.remote.dto.PostDTO
import com.thirfir.domain.BASE_URL
import com.thirfir.domain.BOLD
import com.thirfir.domain.BR_TAG
import com.thirfir.domain.B_TAG
import com.thirfir.domain.COLSPAN
import com.thirfir.domain.DEL_TAG
import com.thirfir.domain.DIV_TAG
import com.thirfir.domain.EM_TAG
import com.thirfir.domain.FONT_SIZE
import com.thirfir.domain.FONT_WEIGHT
import com.thirfir.domain.INS_TAG
import com.thirfir.domain.ITALIC
import com.thirfir.domain.I_TAG
import com.thirfir.domain.LINE_THROUGH
import com.thirfir.domain.P_TAG
import com.thirfir.domain.ROWSPAN
import com.thirfir.domain.SPAN_TAG
import com.thirfir.domain.STRIKE_TAG
import com.thirfir.domain.STRONG_TAG
import com.thirfir.domain.STYLE
import com.thirfir.domain.TABLE_TAG
import com.thirfir.domain.TBODY_TAG
import com.thirfir.domain.TD_TAG
import com.thirfir.domain.TEXT_DECORATION_LINE
import com.thirfir.domain.TR_TAG
import com.thirfir.domain.UNDERLINE
import com.thirfir.domain.U_TAG
import com.thirfir.domain.addQueryString
import com.thirfir.domain.model.element.EnabledRootTag
import com.thirfir.domain.model.element.ParentElement
import com.thirfir.domain.model.element.TableElement
import com.thirfir.domain.model.element.TextElement
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

class PostRemoteDataSourceImpl : PostRemoteDataSource {

    private val parentElements: MutableList<ParentElement> = mutableListOf()
    override suspend fun getPostDTO(bulletin: Int, pid: Int): PostDTO {
        val doc = Jsoup.connect(
                BASE_URL.addQueryString("b", bulletin)
                    .addQueryString("p", pid)).get()
        val mParentElements = doc.select(".bc-s-post-ctnt-area > *")

        mParentElements.forEachIndexed { index, element ->
            if (index >= parentElements.size) {
                parentElements.add(ParentElement(mutableListOf(), EnabledRootTag.P, mutableListOf()))
            }

            val parentStyle = extractStyles(element.attr(STYLE)).apply {
                if(this[FONT_SIZE] == null)
                    this[FONT_SIZE] = "12pt"
            }

            if(element.tagName() == TABLE_TAG) {
                parentElements[index].enabledRootTag = EnabledRootTag.TABLE
                extractTable(element.select(TBODY_TAG)[0], index, parentStyle)
            } else {
                parentElements[index].enabledRootTag = EnabledRootTag.P
                extractTextElements(element, index, parentStyle)
            }
        }
        return PostDTO(parentElements)
    }

    /**
     * @param element 현재 element
     * @param index 최상위 태그 index
     * @param parentStyles 부모 스타일
     * @param pDepth 줄바꿈 횟수
     */
    private fun extractTextElements(element: Element, index: Int, parentStyles: Map<String, String>, pDepth: Int = 0) {

        element.childNodes().forEachIndexed { childIndex, childNode ->
            if(childNode is Element) {
                var styles = extractParentStylesWithItself(childNode, parentStyles)
                if(childNode.tagName().isStyleTag())
                    styles = setStyleOfTag(childNode, index, styles)

                var p = pDepth
                if(childNode.tagName() == P_TAG) {
                    if(childIndex != 0)
                        ++p
                }
                if(childNode.tagName() == BR_TAG) {
                    parentElements[index].textElements.add(TextElement("\n", parentStyles as MutableMap))
                }
                if(childNode.tagName() == TABLE_TAG) {
                    parentElements[index].enabledRootTag = EnabledRootTag.TABLE
                    extractTable(childNode.select(TBODY_TAG)[0], index, styles)
                } else {
                    extractTextElements(childNode, index, styles, p)
                }
            } else if(childNode is TextNode) {
                val newLineDepth = "\n".repeat(pDepth)
                parentElements[index].textElements.add(TextElement(newLineDepth + childNode.wholeText, parentStyles as MutableMap))
            }
        }
    }

    /**
     * @param tbody tbody
     * @param index 최상위 태그(table) index
     * @param parentStyles 부모 스타일
     */
    private fun extractTable(tbody: Element, index: Int, parentStyles: Map<String, String>) {
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
            for(tdIndex in 0 until colSize) {
                if(parentElements[index].tables!![trIndex][tdIndex] == null)
                    continue

                var indexInHtml = tdIndex
                for(i in 0 until tdIndex)
                    if(parentElements[index].tables!![trIndex][i] == null)
                        --indexInHtml

                tr.child(indexInHtml).let { td ->
                    if(td.tagName() == TD_TAG) {
                        var rowSpan = 1
                        val rowSpanAttr = td.attr(ROWSPAN)
                        if(rowSpanAttr.isNotEmpty())
                            rowSpan = rowSpanAttr.toInt()
                        var colSpan = 1
                        val colSpanAttr = td.attr(COLSPAN)
                        if(colSpanAttr.isNotEmpty())
                            colSpan = colSpanAttr.toInt()

                        for(rowIndex in 1 until rowSpan)
                            parentElements[index].tables!![trIndex + rowIndex][tdIndex] = null // rowspan 만큼 아래 칸 무효화
                        for(columnIndex in 1 until colSpan)
                            parentElements[index].tables!![trIndex][tdIndex + columnIndex] = null  // colspan 만큼 오른쪽 칸 무효화
                        parentElements[index].tables!![trIndex][tdIndex]?.rowSpan = rowSpan
                        parentElements[index].tables!![trIndex][tdIndex]?.colSpan = colSpan
                        extractTdTextElements(td, index, trIndex, tdIndex, parentStyles)
                    }
                }
            }
        }
    }

    private fun extractTdTextElements(e: Element, index: Int, rowIndex: Int, colIndex: Int, parentStyles: Map<String, String>) {
        e.children().forEach {
            val styles = extractParentStylesWithItself(it, parentStyles)
            parentElements[index].tables!![rowIndex][colIndex]?.textElement
                ?.add(TextElement(it.wholeOwnText(), styles))
            if(parentElements[index].tables!![rowIndex][colIndex]?.textElement?.size!! > 1)
                if (it.tagName() == P_TAG)
                    parentElements[index].tables!![rowIndex][colIndex]?.textElement?.last()?.text =
                        "\n" + parentElements[index].tables!![rowIndex][colIndex]?.textElement?.last()?.text
            extractTdTextElements(it, index, rowIndex, colIndex, styles)
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

    private fun extractParentStylesWithItself(element: Element, parentStyles: Map<String, String>) : MutableMap<String, String> {
        return extractStyles(element.attr(STYLE)).apply {
            // 자식 스타일 추출한 다음 지정되지 않은 스타일만
            // 부모 스타일 적용
            parentStyles.forEach { parentStyle ->
                if(this[parentStyle.key] == null)
                    this[parentStyle.key] = parentStyle.value
            }
        }
    }

    private fun setStyleOfTag(element: Element, index: Int, styles: MutableMap<String, String>) : MutableMap<String, String> {
        if (element.tagName().trim() == U_TAG || element.tagName().trim() == INS_TAG) {
            if (styles[TEXT_DECORATION_LINE] == null)
                styles[TEXT_DECORATION_LINE] = UNDERLINE
        } else if (element.tagName().trim() == B_TAG || element.tagName().trim() == STRONG_TAG) {
            if (styles[FONT_WEIGHT] == null)
                styles[FONT_WEIGHT] = BOLD
        } else if (element.tagName().trim() == STRIKE_TAG || element.tagName().trim() == DEL_TAG) {
            if (styles[TEXT_DECORATION_LINE] == null)
                styles[TEXT_DECORATION_LINE] = LINE_THROUGH
        } else if (element.tagName().trim() == I_TAG || element.tagName().trim() == EM_TAG) {
            if (styles[FONT_WEIGHT] == null)
                styles[FONT_WEIGHT] = ITALIC
        }
        return styles
    }

    private fun String.isStyleTag() : Boolean {
        return this == B_TAG || this == STRONG_TAG || this == I_TAG || this == EM_TAG || this == U_TAG || this == INS_TAG || this == STRIKE_TAG || this == DEL_TAG
    }
}

