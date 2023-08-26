package com.thirfir.data.datasource.remote.dto

data class PostDTO(
    val htmlElementDTOs: List<HtmlElementDTO>,
    val attachedFileDTOs: List<AttachedFileDTO>,
)