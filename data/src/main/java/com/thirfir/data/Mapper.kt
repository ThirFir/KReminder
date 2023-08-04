package com.thirfir.data

import com.thirfir.data.datasource.local.entitiy.KeywordEntity
import com.thirfir.domain.model.Keyword

fun KeywordEntity.toKeyword() = Keyword(
    name = name,
    createdAt = createdAt,
)

fun Keyword.toKeywordEntity() = KeywordEntity(
    name = name,
    createdAt = createdAt,
)