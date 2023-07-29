package com.thirfir.data.repository

import com.thirfir.data.datasource.remote.PostHeaderRemoteDataSource
import com.thirfir.domain.model.PostHeader
import com.thirfir.domain.repository.PostHeaderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostHeaderRepositoryImpl @Inject constructor(
    private val postHeaderRemoteDataSource: PostHeaderRemoteDataSource
) : PostHeaderRepository {
    override fun getPostHeaders(bulletin: Int, page: Int): Flow<List<PostHeader>> {
        TODO("PostHeaderRemoteDataSource에서 DTO 받고 Model로 변환 후 반환")
    }

}