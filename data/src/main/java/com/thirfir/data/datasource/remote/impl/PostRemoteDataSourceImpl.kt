package com.thirfir.data.datasource.remote.impl

import com.thirfir.data.datasource.remote.PostRemoteDataSource
import com.thirfir.data.datasource.remote.dto.PostDTO
import com.thirfir.domain.BASE_URL
import com.thirfir.domain.BOLD
import com.thirfir.domain.FONT_WEIGHT
import com.thirfir.domain.TEXT_DECORATION_LINE
import com.thirfir.domain.UNDERLINE
import com.thirfir.domain.addQueryString
import com.thirfir.domain.model.EnabledRootTag
import com.thirfir.domain.model.ParentElement
import com.thirfir.domain.model.TableElement
import com.thirfir.domain.model.TextElement
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
            if(element.tagName() == "p") {
                parentElements[index].enabledRootTag = EnabledRootTag.P
                extractTextElements(element, index)
            }
            else if(element.tagName() == "table") {
                parentElements[index].enabledRootTag = EnabledRootTag.TABLE
                extractTable(element.select("tbody")[0], index)
            }
            else if(element.tagName() == "h3")
                parentElements[index].enabledRootTag = EnabledRootTag.H3

            extractTextElements(element, index)
        }

        return PostDTO(parentElements)
    }

    /**
     * @param element : 현재 element
     * @param index : 최상위 태그 index
     * @param u : 현재 element가 underline인지
     * @param b : 현재 element가 bold인지
     */
    private fun extractTextElements(element: Element, index: Int, u: Boolean = false, b: Boolean = false) {


        // 모든 자식 element 순환
        /**
         * @param it : 현재 태그
         */

        element.children().forEach {
            var underline = u
            var bold = b
            if (it.tagName().trim() == "u")
                underline = true
            else if (it.tagName().trim() == "b")
                bold = true
            parentElements[index].textElements.add(TextElement(it.ownText(), extractStyles(it.attr("style"))))
            if (underline)
                parentElements[index].textElements[parentElements[index].textElements.lastIndex].style[TEXT_DECORATION_LINE] =
                    UNDERLINE
            if (bold)
                parentElements[index].textElements[parentElements[index].textElements.lastIndex].style[FONT_WEIGHT] = BOLD

            extractTextElements(it, index, underline, bold)
        }
    }

    private fun extractTable(tbody: Element, index: Int) {
        // 테이블 사이즈 구하기 및 초기화
        var rowSize = getTableHeight(tbody)
        var colSize = getTableWidth(tbody)
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

    private fun extractTdTextElements(td: Element, index: Int, rowIndex: Int, colIndex: Int) {
        td.children().forEach {
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



    private fun processTableElement(element: Element, index: Int) {
        val tableData: MutableList<MutableList<String>> = mutableListOf()

        val rows = element.select("tr")
        rows.forEach { rowElement ->
            val rowData: MutableList<String> = mutableListOf()

            val cells = rowElement.select("td")
            cells.forEach { cellElement ->
                rowData.add(cellElement.text())
            }

            tableData.add(rowData)
        }
        val tableText = tableData.joinToString("\n") { row ->
            row.joinToString("\t")
        }

        val tableStyles = extractStyles(element.attr("style")) // Get styles for the table
        //textElements[index].add(TextElement(tableText, tableStyles))
    }

}



