package com.thirfir.data.repository

import com.thirfir.data.datasource.remote.PostRemoteDataSource
import com.thirfir.data.mapper.PostMapper
import com.thirfir.domain.model.Post
import com.thirfir.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postRemoteDataSource: PostRemoteDataSource
) : PostRepository {
    override fun getPost(bulletin: Int, pid: Int): Flow<Post> {
        return PostMapper.mapperToPost(
            postRemoteDataSource.getPostDTO(bulletin, pid)
        )
    }
}