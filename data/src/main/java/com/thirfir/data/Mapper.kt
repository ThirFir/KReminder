package com.thirfir.data

import com.thirfir.data.datasource.remote.dto.PostDTO
import com.thirfir.data.datasource.remote.dto.PostHeaderDTO
import com.thirfir.domain.model.Post
import com.thirfir.domain.model.PostHeader
import com.thirfir.data.datasource.local.entitiy.KeywordEntity
import com.thirfir.domain.model.Keyword
import com.thirfir.data.datasource.remote.dto.HtmlElementDTO
import com.thirfir.domain.BACKGROUND_COLOR
import com.thirfir.domain.BLACK
import com.thirfir.domain.COLOR
import com.thirfir.domain.FONT_SIZE
import com.thirfir.domain.FONT_WEIGHT
import com.thirfir.domain.MARGIN
import com.thirfir.domain.NORMAL
import com.thirfir.domain.PADDING
import com.thirfir.domain.START
import com.thirfir.domain.STYLE
import com.thirfir.domain.TEXT_ALIGN
import com.thirfir.domain.TRANSPARENT
import com.thirfir.domain.model.HtmlElement

fun PostHeaderDTO.toPostHeader(): PostHeader =
    PostHeader(
        pid = pid,
        title = title,
        author = author,
        date = date,
        isTopFixed = isTopFixed
    )

fun PostDTO.toPost(): Post =
    Post(
        htmlElements = htmlElementDTOs.map { it.toHtmlElement(
            mutableMapOf(FONT_SIZE to "12pt",
                BACKGROUND_COLOR to TRANSPARENT,
                COLOR to BLACK,
                TEXT_ALIGN to START,
                FONT_WEIGHT to NORMAL,
                PADDING to "0px",
                MARGIN to "0px"
                )
        ) },
    )

fun KeywordEntity.toKeyword() = Keyword(
    name = name,
    createdAt = createdAt,
)

fun Keyword.toKeywordEntity() = KeywordEntity(
    name = name,
    createdAt = createdAt,
)

fun HtmlElementDTO.toHtmlElement(parentStyles: MutableMap<String, String> = mutableMapOf()) : HtmlElement {
    val styles = extractStyles(attributes[STYLE], parentStyles)
    return HtmlElement(
        tag = tag,
        text = text,
        attributes = attributes,
        styles = styles,
        childElements = childElement?.map { it.toHtmlElement(styles) }?.toMutableList(),
    )
}

private fun extractStyles(attrString: String?, parentStyles: MutableMap<String, String>) : MutableMap<String, String> {
    if(attrString == null) return parentStyles

    val styles = mutableMapOf<String, String>()
    parentStyles.forEach { (key, value) ->
        styles[key] = value
    }

    val styleAttrs = attrString.split(";")
    for (style in styleAttrs) {
        val parts = style.trim().split(":")

        if (parts.size == 2) {
            styles[parts[0].trim().lowercase()] = parts[1].trim().lowercase()
        }
    }

    return styles
}