package com.thirfir.data.repository

import com.thirfir.data.datasource.remote.PostRemoteDataSource
import com.thirfir.domain.model.Post
import com.thirfir.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postRemoteDataSource: PostRemoteDataSource
) : PostRepository {
    override fun getPost(pid: Int) {

    }
}