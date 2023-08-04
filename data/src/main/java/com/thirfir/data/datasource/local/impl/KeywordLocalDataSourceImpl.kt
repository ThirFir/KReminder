package com.thirfir.data.datasource.local.impl

import com.thirfir.data.datasource.local.KeywordLocalDataSource
import com.thirfir.data.datasource.local.dao.KeywordDao
import com.thirfir.data.datasource.local.entitiy.KeywordEntity
import javax.inject.Inject

class KeywordLocalDataSourceImpl @Inject constructor(
    private val dao: KeywordDao?
) : KeywordLocalDataSource {
    override suspend fun getKeywords(): List<KeywordEntity> {
        return dao?.getAll() ?: emptyList()
    }

    override suspend fun insertKeyword(keyword: KeywordEntity) {
        dao?.insert(keyword)
    }

    override suspend fun deleteKeyword(name: String) {
        dao?.delete(name)
    }

    override suspend fun deleteAll() {
        dao?.deleteAll()
    }
}