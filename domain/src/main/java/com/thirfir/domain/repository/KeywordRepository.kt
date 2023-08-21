package com.thirfir.domain.repository

import com.thirfir.domain.model.Keyword

interface KeywordRepository {
    suspend fun getKeywords() : List<Keyword>
    suspend fun insertKeyword(keyword: Keyword)
    suspend fun deleteKeyword(name: String)
}