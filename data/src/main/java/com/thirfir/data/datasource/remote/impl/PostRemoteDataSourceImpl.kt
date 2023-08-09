package com.thirfir.data.datasource.remote.impl

import com.thirfir.data.datasource.remote.PostRemoteDataSource
import com.thirfir.data.datasource.remote.dto.PostDTO
import com.thirfir.domain.BASE_URL
import com.thirfir.domain.BOLD
import com.thirfir.domain.BOLD_TAG
import com.thirfir.domain.COLSPAN
import com.thirfir.domain.EM_TAG
import com.thirfir.domain.FONT_WEIGHT
import com.thirfir.domain.ITALIC
import com.thirfir.domain.ITALIC_TAG
import com.thirfir.domain.LINE_THROUGH
import com.thirfir.domain.P_TAG
import com.thirfir.domain.ROWSPAN
import com.thirfir.domain.STRIKE_TAG
import com.thirfir.domain.STRONG_TAG
import com.thirfir.domain.STYLE
import com.thirfir.domain.TABLE_TAG
import com.thirfir.domain.TBODY_TAG
import com.thirfir.domain.TD_TAG
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
            if(element.tagName() == TABLE_TAG) {
                parentElements[index].enabledRootTag = EnabledRootTag.TABLE
                extractTable(element.select(TBODY_TAG)[0], index, extractStyles(element.attr(STYLE)))
            } else {
                parentElements[index].enabledRootTag = EnabledRootTag.P
                extractTextElements(
                    element,
                    index,
                    extractStyles(element.attr(STYLE))
                )
            }

            parentElements[index].textElements = extractSubstrings(element.text(), parentElements[index].textElements)
        }

        return PostDTO(parentElements)
    }

    /**
     * @param element 현재 element
     * @param index 최상위 태그 index
     * @param parentStyles 부모 스타일
     */
    private fun extractTextElements(element: Element, index: Int, parentStyles: Map<String, String>) {


        // 모든 자식 element 순환
        element.children().forEach {
            val styles = extractParentStylesWithItself(it, parentStyles)
            parentElements[index].textElements.add(TextElement(it.wholeOwnText(), styles).apply {
                if(it.tagName() == P_TAG)
                    this.text = "\n" + this.text
            })
            setStyleOfTag(it, index)
            
            if(it.tagName() == TABLE_TAG) {
                parentElements[index].enabledRootTag = EnabledRootTag.TABLE
                extractTable(it.select(TBODY_TAG)[0], index, styles)
            }
            else
                extractTextElements(it, index, styles)
        }
    }

    /**
     * @param tbody tbody
     * @param index 최상위 태그(table) index
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
                ?.add(TextElement(it.ownText(), styles).apply {
                    if(it.tagName() == P_TAG)
                        this.text = "\n" + this.text
                })
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

    // "style:..." 에 나타나 있는 것들만 추출함
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
            // 자식 뷰에 부모 스타일 적용
            parentStyles.forEach { parentStyle ->
                if(this[parentStyle.key] == null)
                    this[parentStyle.key] = parentStyle.value
            }
        }
    }

    private fun setStyleOfTag(element: Element, index: Int) {
        if (element.tagName().trim() == UNDERLINE_TAG)
            parentElements[index].textElements[parentElements[index].textElements.lastIndex].style[TEXT_DECORATION_LINE] =
                UNDERLINE
        else if (element.tagName().trim() == BOLD_TAG || element.tagName().trim() == STRONG_TAG)
            parentElements[index].textElements[parentElements[index].textElements.lastIndex].style[FONT_WEIGHT] =
                BOLD
        else if (element.tagName().trim() == STRIKE_TAG)
            parentElements[index].textElements[parentElements[index].textElements.lastIndex].style[TEXT_DECORATION_LINE] =
                LINE_THROUGH
        else if (element.tagName().trim() == ITALIC_TAG || element.tagName().trim() == EM_TAG)
            parentElements[index].textElements[parentElements[index].textElements.lastIndex].style[FONT_WEIGHT] =
                ITALIC
    }

    private fun extractSubstrings(baseText: String, elements: MutableList<TextElement>): MutableList<TextElement> {
        val sortedElements = elements.sortedBy { baseText.indexOf(it.text.trim()) }
        return sortedElements.toMutableList()
    }
}



