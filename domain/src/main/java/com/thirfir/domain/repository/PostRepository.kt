package com.thirfir.domain.repository

import com.thirfir.domain.model.Post

interface PostRepository {
    suspend fun getPost(bulletin: Int, pid: Int) : Post
}