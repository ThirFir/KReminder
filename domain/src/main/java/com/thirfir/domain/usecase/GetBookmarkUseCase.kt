package com.thirfir.domain.usecase

import com.thirfir.domain.IoDispatcher
import com.thirfir.domain.model.Bookmark
import com.thirfir.domain.repository.BookmarkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val ioScope: CoroutineScope = CoroutineScope(ioDispatcher)
) {
    suspend fun getBookmarks() : List<Bookmark> {
        return withContext(ioDispatcher) {
            bookmarkRepository.getBookmarks()
        }
    }

    fun insertBookmark(bookmark: Bookmark, onComplete: (Bookmark) -> Unit) {
        ioScope.launch {
            bookmarkRepository.insertBookmark(bookmark)
            onComplete(bookmark)
        }
    }

    fun deleteBookmark(pid: Int, onComplete: () -> Unit) {
        ioScope.launch {
            bookmarkRepository.deleteBookmark(pid)
            onComplete()
        }
    }
}