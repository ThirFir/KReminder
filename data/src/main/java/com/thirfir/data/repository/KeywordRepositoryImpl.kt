package com.thirfir.data.repository


import com.thirfir.data.datasource.local.KeywordLocalDataSource
import com.thirfir.data.toKeyword
import com.thirfir.data.toKeywordEntity
import com.thirfir.domain.model.Keyword
import com.thirfir.domain.repository.KeywordRepository
import javax.inject.Inject

class KeywordRepositoryImpl @Inject constructor(
    private val keywordLocalDataSource: KeywordLocalDataSource,
) : KeywordRepository {
    override suspend fun getKeywords(): List<Keyword> {
        return keywordLocalDataSource.getKeywords().map {
            it.toKeyword()
        }
    }

    override suspend fun insertKeyword(keyword: Keyword) {
        keywordLocalDataSource.insertKeyword(keyword.toKeywordEntity())
    }

    override suspend fun deleteKeyword(name: String) {
        keywordLocalDataSource.deleteKeyword(name)
    }

    override suspend fun deleteAll() {
        keywordLocalDataSource.deleteAll()
    }

}