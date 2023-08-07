package com.thirfir.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thirfir.domain.model.Post
import com.thirfir.domain.usecase.GetPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getPostUseCase: GetPostUseCase
) : ViewModel() {

    init {
        fetchPost(14,30975) {    // 페이지, 글번호
            Log.d("PostViewModel", it.textElements.toString())
        }
    }
    fun fetchPost(bulletin: Int, pid: Int, onResponseCallback: (Post) -> Unit) {
        viewModelScope.launch {
            onResponseCallback(getPostUseCase(bulletin, pid))
        }
    }
}