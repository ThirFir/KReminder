package com.thirfir.domain.usecase

import com.thirfir.domain.model.PostHeader
import com.thirfir.domain.repository.PostHeaderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostHeaderUseCase @Inject constructor(
    private val postHeaderRepository: PostHeaderRepository
) {
    operator fun invoke(bulletin: Int, page: Int) {

    }
}