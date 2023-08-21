package com.thirfir.data.datasource.local.keyword

import com.thirfir.data.datasource.local.keyword.entitiy.KeywordEntity

interface KeywordLocalDataSource {
    suspend fun getKeywords() : List<KeywordEntity>
    suspend fun insertKeyword(keyword: KeywordEntity)
    suspend fun deleteKeyword(name: String)
}