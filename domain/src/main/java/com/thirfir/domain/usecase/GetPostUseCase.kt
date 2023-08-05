package com.thirfir.domain.usecase

import com.thirfir.domain.model.Post
import com.thirfir.domain.repository.PostRepository
import javax.inject.Inject

class GetPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    operator fun invoke(bulletin: Int, pid: Int) : Post {
        return postRepository.getPost(bulletin, pid)
    }
}