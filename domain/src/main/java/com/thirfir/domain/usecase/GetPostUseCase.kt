package com.thirfir.domain.usecase

import com.thirfir.domain.model.Post
import com.thirfir.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    operator fun invoke(pid: Int) {

    }
}