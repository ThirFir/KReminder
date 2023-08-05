package com.thirfir.data.repository

import com.thirfir.data.datasource.remote.PostHeaderRemoteDataSource
import com.thirfir.data.toPostHeader
import com.thirfir.domain.model.PostHeader
import com.thirfir.domain.repository.PostHeaderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostHeaderRepositoryImpl @Inject constructor(
    private val postHeaderRemoteDataSource: PostHeaderRemoteDataSource
) : PostHeaderRepository {
    override fun getPostHeaders(bulletin: Int, page: Int): Flow<List<PostHeader>> {
        return postHeaderRemoteDataSource.getPostHeadersDTO(bulletin, page)
            .map {dtoList -> dtoList.map{ it.toPostHeader() } }
    }

}