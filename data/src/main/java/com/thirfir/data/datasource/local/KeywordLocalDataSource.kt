package com.thirfir.data.datasource.local

import com.thirfir.data.datasource.local.entitiy.KeywordEntity

interface KeywordLocalDataSource {
    suspend fun getKeywords() : List<KeywordEntity>
    suspend fun insertKeyword(keyword: KeywordEntity)
    suspend fun deleteKeyword(name: String)
    suspend fun deleteAll()
}