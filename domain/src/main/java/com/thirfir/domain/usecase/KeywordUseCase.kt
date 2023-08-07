package com.thirfir.domain.usecase

import com.thirfir.domain.IoDispatcher
import com.thirfir.domain.model.Keyword
import com.thirfir.domain.repository.KeywordRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class KeywordUseCase @Inject constructor(
    private val keywordRepository: KeywordRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val ioScope: CoroutineScope = CoroutineScope(ioDispatcher)
) {
    suspend fun getKeywords() : List<Keyword> {
        return withContext(ioDispatcher) {
            keywordRepository.getKeywords()
        }
    }

    fun insertKeyword(name: String, onComplete: (Keyword) -> Unit) {
        val keyword = Keyword(name, System.currentTimeMillis())
        ioScope.launch {
            keywordRepository.insertKeyword(keyword)
            onComplete(keyword)
        }
    }

    fun deleteKeyword(name: String, onComplete: () -> Unit) {
        ioScope.launch {
            keywordRepository.deleteKeyword(name)
            onComplete()
        }
    }

    fun deleteAll(onComplete: () -> Unit) {
        ioScope.launch {
            keywordRepository.deleteAll()
            onComplete()
        }
    }
}