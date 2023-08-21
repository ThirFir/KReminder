package com.thirfir.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thirfir.domain.model.Keyword
import com.thirfir.domain.usecase.KeywordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KeywordsViewModel @Inject constructor(
    private val keywordUseCase: KeywordUseCase
) : ViewModel() {

    private val _keywords: MutableLiveData<List<Keyword>> = MutableLiveData()
    val keywords: LiveData<List<Keyword>> get() = _keywords

    init {
        getKeywords()
    }

    private fun getKeywords() {
        viewModelScope.launch {
            _keywords.value = keywordUseCase.getKeywords()
        }
    }

    fun insertKeyword(name: String) {
        keywordUseCase.insertKeyword(name) {
            _keywords.postValue(_keywords.value?.plus(it))
        }
    }

    fun deleteKeyword(name: String) {
        keywordUseCase.deleteKeyword(name) {
            _keywords.postValue(_keywords.value?.filter { it.name != name })
        }
    }
}