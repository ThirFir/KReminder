package com.thirfir.domain.repository

import com.thirfir.domain.model.PostHeader
import kotlinx.coroutines.flow.Flow

interface PostHeaderRepository {
    fun getPostHeaders(bulletin: Int, page: Int): Flow<List<PostHeader>>
}