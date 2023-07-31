package com.thirfir.domain.repository

import com.thirfir.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPost(bulletin: Int, pid: Int) : Flow<Post>
}