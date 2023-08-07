package com.thirfir.data.datasource.remote

import com.thirfir.data.datasource.remote.dto.PostHeaderDTO
import kotlinx.coroutines.flow.Flow

interface PostHeaderRemoteDataSource {

    fun getPostHeadersDTO(bulletin: Int, page: Int): Flow<List<PostHeaderDTO>>
}