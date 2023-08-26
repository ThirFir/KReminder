package com.thirfir.data.datasource.remote.impl

import android.util.Log
import com.thirfir.data.datasource.remote.PostRemoteDataSource
import com.thirfir.data.datasource.remote.dto.AttachedFileDTO
import com.thirfir.data.datasource.remote.dto.PostDTO
import com.thirfir.domain.BASE_URL
import com.thirfir.data.datasource.remote.dto.HtmlElementDTO
import com.thirfir.domain.BULLETIN_QUERY
import com.thirfir.domain.HREF
import com.thirfir.domain.PID_QUERY
import com.thirfir.domain.util.addQueryString
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import javax.security.auth.Subject.getSubject

class PostRemoteDataSourceImpl : PostRemoteDataSource {

    private lateinit var doc: Document
    override suspend fun getPostDTO(bulletin: Int, pid: Int): PostDTO {
        doc = Jsoup.connect(
            BASE_URL.addQueryString(BULLETIN_QUERY, bulletin)
                .addQueryString(PID_QUERY, pid)).get()
        val parentElements = doc.select(".bc-s-post-ctnt-area > *")

        val htmlElementDTOs = mutableListOf<HtmlElementDTO>()
        parentElements.forEach { rootElement ->
            htmlElementDTOs.add(extractHtmlElement(rootElement))
        }
        return PostDTO(htmlElementDTOs.toList(), getAttachedFileDTOs())
    }

    override suspend fun getAttachedFileDTOs(): List<AttachedFileDTO> {
        val attachedFileElements = doc.select(".tx-attach-main > #tx_attach_list > li > dl > dt")
        val attachedFileDTOs = mutableListOf<AttachedFileDTO>()
        attachedFileElements.forEach { dt ->
            val file = dt.children().first()
            val name = file?.text()
            val href = file?.attr(HREF)
            attachedFileDTOs.add(AttachedFileDTO(name, href))
        }
        return attachedFileDTOs.toList()
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