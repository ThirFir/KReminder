package com.thirfir.data

import com.thirfir.data.datasource.remote.dto.PostDTO
import com.thirfir.data.datasource.remote.dto.PostHeaderDTO
import com.thirfir.domain.model.Post
import com.thirfir.domain.model.PostHeader

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
        body = body,
        file = file
    )