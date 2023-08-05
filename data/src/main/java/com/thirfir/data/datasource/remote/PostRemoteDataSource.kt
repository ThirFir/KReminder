package com.thirfir.data.datasource.remote

import com.thirfir.data.datasource.remote.dto.PostDTO

interface PostRemoteDataSource {

    suspend fun getPostDTO(bulletin: Int, pid: Int) : PostDTO
}