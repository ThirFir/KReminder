package com.thirfir.data.datasource.remote

import com.thirfir.data.datasource.remote.dto.PostHeaderDTO
import kotlinx.coroutines.flow.Flow

class PostHeaderRemoteDataSource {

    fun getPostHeaders(bulletin: Int, page: Int): Flow<List<PostHeaderDTO>> {
        TODO("서버 연결, 받아온걸 DTO로 변환 후 반환")
    }
}