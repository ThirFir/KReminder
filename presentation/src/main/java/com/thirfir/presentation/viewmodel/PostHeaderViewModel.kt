package com.thirfir.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thirfir.domain.model.PostHeader
import com.thirfir.domain.usecase.GetPostHeaderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostHeaderViewModel @Inject constructor(
    private val getPostHeadersUseCase: GetPostHeaderUseCase
) : ViewModel() {
    private val _postHeaders: MutableStateFlow<List<PostHeader>> = MutableStateFlow(emptyList())
    val postHeaders: StateFlow<List<PostHeader>> get() = _postHeaders.asStateFlow()

    init {
        fetchPostHeaders(14, 1) // TODO : bulletin번호 -> AssistedInject ?
    }

    fun fetchPostHeaders(bulletin: Int, page: Int) {
        viewModelScope.launch {
            _postHeaders.value = emptyList()
            getPostHeadersUseCase(bulletin, page).collect { postHeaders ->
                _postHeaders.emit(postHeaders)
            }
        }
    }
}