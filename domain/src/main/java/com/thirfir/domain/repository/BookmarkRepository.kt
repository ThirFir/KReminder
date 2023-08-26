package com.thirfir.domain.repository

import com.thirfir.domain.model.Bookmark

interface BookmarkRepository {
    suspend fun getBookmarks() : List<Bookmark>
    suspend fun insertBookmark(bookmark: Bookmark)
    suspend fun deleteBookmark(pid: Int)
}