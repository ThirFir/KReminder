package com.thirfir.data.datasource.local

import com.thirfir.data.datasource.local.entity.BookmarkEntity

interface BookmarkLocalDataSource {
    suspend fun getBookmarks() : List<BookmarkEntity>
    suspend fun insertBookmark(bookmarkEntity: BookmarkEntity)
    suspend fun deleteBookmark(pid: Int)
}