package com.thirfir.data.datasource.remote.impl

import android.util.Log
import com.thirfir.data.datasource.remote.PostRemoteDataSource
import com.thirfir.data.datasource.remote.dto.PostDTO
import com.thirfir.domain.BASE_URL
import com.thirfir.domain.addQueryString
import com.thirfir.data.datasource.remote.dto.HtmlElementDTO
import com.thirfir.domain.B
import com.thirfir.domain.STRONG
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

class PostRemoteDataSourceImpl : PostRemoteDataSource {

    override suspend fun getPostDTO(bulletin: Int, pid: Int): PostDTO {
        val doc = Jsoup.connect(
            BASE_URL.addQueryString("b", bulletin)
                .addQueryString("p", pid)).get()
        val mParentElements = doc.select(".bc-s-post-ctnt-area > *")

        val htmlElementDTOs = mutableListOf<HtmlElementDTO>()
        mParentElements.forEach { rootElement ->
            htmlElementDTOs.add(extractHtmlElement(rootElement))
        }
        return PostDTO(htmlElementDTOs.toList())
    }

    private fun extractHtmlElement(node: Node) : HtmlElementDTO {
        val childHtmlElementDTOs = mutableListOf<HtmlElementDTO>()
        node.childNodes().forEach {
            if(it is TextNode) {
                childHtmlElementDTOs.add(HtmlElementDTO(null, it.wholeText, mutableMapOf(), null))
            } else if(it is Element) {
                childHtmlElementDTOs.add(extractHtmlElement(it))
            }
        }
        return when(node) {
            is TextNode -> HtmlElementDTO(null, node.wholeText, mutableMapOf(), null)
            is Element -> HtmlElementDTO(node.tagName(), null, extractAttrs(node), childHtmlElementDTOs)
            else -> HtmlElementDTO(null, null, mutableMapOf(), null)
        }
    }

    private fun extractAttrs(node: Node) : MutableMap<String, String> {
        val attrs = mutableMapOf<String, String>()
        node.attributes().forEach {
            attrs[it.key] = it.value
        }
        return attrs
    }
}