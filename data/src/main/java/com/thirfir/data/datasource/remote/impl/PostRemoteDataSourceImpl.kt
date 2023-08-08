package com.thirfir.data.datasource.remote.impl

import com.thirfir.data.datasource.remote.PostRemoteDataSource
import com.thirfir.data.datasource.remote.dto.PostDTO
import com.thirfir.domain.BASE_URL
import com.thirfir.domain.BOLD
import com.thirfir.domain.FONT_WEIGHT
import com.thirfir.domain.TEXT_DECORATION_LINE
import com.thirfir.domain.UNDERLINE
import com.thirfir.domain.addQueryString
import com.thirfir.domain.model.TextElement
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class PostRemoteDataSourceImpl : PostRemoteDataSource {

    private val textElements: MutableList<MutableList<TextElement>> = mutableListOf()

    override suspend fun getPostDTO(bulletin: Int, pid: Int): PostDTO {
        val doc =
            Jsoup.connect(
                BASE_URL.addQueryString("b", bulletin)
                    .addQueryString("p", pid)).get()
        val parentElements = doc.select(".bc-s-post-ctnt-area > *")

        parentElements.forEachIndexed { index, element ->
            extractTextElements(element, index)
        }

        return PostDTO(textElements)
    }
    private fun extractTextElements(element: Element, index: Int, u: Boolean = false, b: Boolean = false) {
        if (index >= textElements.size) {
            textElements.add(mutableListOf())
        }
        element.children().forEach {
            var underline = u
            var bold = b
            if (element.tagName() == "table") {
                processTableElement(element, index)
            }
            else if (it.tagName().trim() == "u")
                underline = true
            else if (it.tagName().trim() == "b")
                bold = true
            textElements[index].add(TextElement(it.ownText(), extractStyles(it.attr("style"))))
            if (underline)
                textElements[index][textElements[index].lastIndex].style[TEXT_DECORATION_LINE] =
                    UNDERLINE
            if (bold)
                textElements[index][textElements[index].lastIndex].style[FONT_WEIGHT] = BOLD

            extractTextElements(it, index, underline, bold)
        }
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
        textElements[index].add(TextElement(tableText, tableStyles))
    }

}