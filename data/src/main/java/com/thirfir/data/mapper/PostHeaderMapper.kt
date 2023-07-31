package com.thirfir.data.mapper

import com.thirfir.data.datasource.remote.dto.PostHeaderDTO
import com.thirfir.domain.model.PostHeader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object PostHeaderMapper {
    fun mapperToPostHeader(flow: Flow<List<PostHeaderDTO>>): Flow<List<PostHeader>> {
        return flow.map {
            it.map { dto ->
                mapToPostHeader(dto)
            }
        }
    }

    fun mapToPostHeader(dto: PostHeaderDTO): PostHeader {
        return dto.run {
            PostHeader(
                pid = pid,
                title = title,
                author = author,
                date = date,
                isTopFixed = isTopFixed
            )
        }
    }
}