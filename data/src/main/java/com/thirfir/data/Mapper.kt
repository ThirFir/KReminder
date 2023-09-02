package com.thirfir.data

import com.thirfir.data.datasource.local.entity.BookmarkEntity
import com.thirfir.data.datasource.remote.dto.PostDTO
import com.thirfir.data.datasource.remote.dto.PostHeaderDTO
import com.thirfir.domain.model.Post
import com.thirfir.domain.model.PostHeader
import com.thirfir.data.datasource.remote.dto.AttachedFileDTO
import com.thirfir.data.datasource.local.entity.KeywordEntity
import com.thirfir.domain.model.Keyword
import com.thirfir.data.datasource.remote.dto.HtmlElementDTO
import com.thirfir.domain.FONT_SIZE
import com.thirfir.domain.KOREATECH_PORTAL_URL
import com.thirfir.domain.MARGIN
import com.thirfir.domain.PADDING
import com.thirfir.domain.STYLE
import com.thirfir.domain.model.AttachedFile
import com.thirfir.domain.model.Bookmark
import com.thirfir.domain.model.HtmlElement
import com.thirfir.domain.model.Settings

fun SettingsProto.toSettings(): Settings =
    Settings(
        allowNotification = allowNotification
    )

fun BookmarkEntity.toBookmark(): Bookmark =
    Bookmark(
        bulletin = bulletin,
        pid = pid,
        title = title,
        category = category,
        timestamp = timestamp,
    )

fun Bookmark.toBookmarkEntity(): BookmarkEntity =
    BookmarkEntity(
        bulletin = bulletin,
        pid = pid,
        title = title,
        category = category,
        timestamp = timestamp,
    )

fun PostHeaderDTO.toPostHeader(): PostHeader =
    PostHeader(
        pid = pid,
        title = title,
        author = author,
        date = date,
        isTopFixed = isTopFixed,
        category = category,
        highlight = highlight,
        viewCount = viewCount.toInt(),
    )

fun PostDTO.toPost(): Post =
    Post(
        htmlElements = htmlElementDTOs.map { it.toHtmlElement(
            mutableMapOf(FONT_SIZE to "12pt",
                PADDING to "0px",
                MARGIN to "0px"
                )
        ) },
        attachedFiles = attachedFileDTOs.map { it.toAttachedFile() },
    )

fun AttachedFileDTO.toAttachedFile() = AttachedFile(
    name = name ?: "Unknown",
    url = KOREATECH_PORTAL_URL + href
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