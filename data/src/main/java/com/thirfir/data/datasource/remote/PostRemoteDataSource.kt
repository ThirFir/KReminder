package com.thirfir.data.datasource.remote

import com.thirfir.data.datasource.remote.dto.PostDTO
import kotlinx.coroutines.flow.Flow

interface PostRemoteDataSource {

    fun getPostDTO(bulletin: Int, pid: Int) : Flow<PostDTO>
}