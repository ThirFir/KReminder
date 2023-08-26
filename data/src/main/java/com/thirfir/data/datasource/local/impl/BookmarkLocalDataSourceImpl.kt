package com.thirfir.data.datasource.local.impl

import com.thirfir.data.datasource.local.BookmarkLocalDataSource
import com.thirfir.data.datasource.local.dao.BookmarkDao
import com.thirfir.data.datasource.local.entity.BookmarkEntity
import javax.inject.Inject

class BookmarkLocalDataSourceImpl @Inject constructor(
    private val dao: BookmarkDao?
) : BookmarkLocalDataSource {
    override suspend fun getBookmarks(): List<BookmarkEntity> {
        return dao?.getAll() ?: emptyList()
    }

    override suspend fun insertBookmark(bookmarkEntity: BookmarkEntity) {
        dao?.insert(bookmarkEntity)
    }

    override suspend fun deleteBookmark(pid: Int) {
        dao?.delete(pid)
    }
}