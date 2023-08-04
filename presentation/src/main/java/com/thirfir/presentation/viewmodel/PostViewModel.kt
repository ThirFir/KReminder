package com.thirfir.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thirfir.domain.model.Post
import com.thirfir.domain.usecase.GetPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getPostUseCase: GetPostUseCase
) : ViewModel() {

    fun fetchPost(bulletin: Int, pid: Int, onResponseCallback: (Flow<Post>) -> Unit) {
        viewModelScope.launch {
            onResponseCallback(getPostUseCase(bulletin, pid))
        }
    }
}