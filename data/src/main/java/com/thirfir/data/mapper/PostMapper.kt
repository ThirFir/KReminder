package com.thirfir.data.mapper

import com.thirfir.data.datasource.remote.dto.PostDTO
import com.thirfir.domain.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object PostMapper {
    fun mapperToPost(flow: Flow<PostDTO>): Flow<Post> {
        return flow.map {
            mapToPost(it)
        }
    }

    private fun mapToPost(dto: PostDTO): Post {
        return dto.run {
            Post(
                header = PostHeaderMapper.mapToPostHeader(headerDTO),
                body = body,
                file = file
            )
        }
    }
}