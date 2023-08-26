package com.thirfir.data.datasource.local

import com.thirfir.data.datasource.local.entity.KeywordEntity

interface KeywordLocalDataSource {
    suspend fun getKeywords() : List<KeywordEntity>
    suspend fun insertKeyword(keyword: KeywordEntity)
    suspend fun deleteKeyword(name: String)
}