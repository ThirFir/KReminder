package com.thirfir.domain.usecase

import com.thirfir.domain.IoDispatcher
import com.thirfir.domain.model.Post
import com.thirfir.domain.repository.PostRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPostUseCase @Inject constructor(
    private val postRepository: PostRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(bulletin: Int, pid: Int) : Post {
        return withContext(ioDispatcher) {
            postRepository.getPost(bulletin, pid)
        }
    }
}