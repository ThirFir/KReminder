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
fun main() {
    val html = "<div><p style=\"font-family: 맑은고딕, 'Malgun Gothic', 돋움, Dotum, AppleGothic, sans-serif; line-height: 1.8; font-size: 13.33px;\"><span style=\"margin: 0px; padding: 0px; color: rgb(0, 0, 0); font-family: '맑은 고딕', AppleGothic, sans-serif; font-size: 11pt;\"><b>Chat GPT를 활용한 생성형 AI 교육</b>에 관심 있는 학부(과) 재학생 및 대학원생들의 많은 참여 바랍니다.</span></p></div>"
    val doc = Jsoup.parse(html)
    val mParentElements = doc.select("div > *")
    mParentElements.forEach { r ->
        r.childNodes().forEach {
            d(it)
        }
    }
}

fun d(node: Node) {
    if(node is Element) {
        println("dd    " + node)
        node.childNodes().forEach {
            d(it)
        }
    } else if(node is TextNode) {
        println(node.wholeText)
    }
}