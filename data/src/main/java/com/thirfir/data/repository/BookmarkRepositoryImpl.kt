package com.thirfir.data.repository

import com.thirfir.data.datasource.local.BookmarkLocalDataSource
import com.thirfir.data.toBookmark
import com.thirfir.data.toBookmarkEntity
import com.thirfir.domain.model.Bookmark
import com.thirfir.domain.repository.BookmarkRepository
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkLocalDataSource: BookmarkLocalDataSource,
) : BookmarkRepository {
    override suspend fun getBookmarks(): List<Bookmark> {
        return bookmarkLocalDataSource.getBookmarks().map {
            it.toBookmark()
        }
    }

    override suspend fun insertBookmark(bookmark: Bookmark) {
        bookmarkLocalDataSource.insertBookmark(bookmark.toBookmarkEntity())
    }

    override suspend fun deleteBookmark(pid: Int) {
        bookmarkLocalDataSource.deleteBookmark(pid)
    }
}