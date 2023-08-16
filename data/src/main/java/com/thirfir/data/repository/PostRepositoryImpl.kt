package com.thirfir.data.repository

import android.util.Log
import com.thirfir.data.datasource.remote.PostRemoteDataSource
import com.thirfir.data.toPost
import com.thirfir.domain.model.Post
import com.thirfir.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postRemoteDataSource: PostRemoteDataSource
) : PostRepository {
    override suspend fun getPost(bulletin: Int, pid: Int): Post {
        return postRemoteDataSource.getPostDTO(bulletin, pid).toPost()
    }
}