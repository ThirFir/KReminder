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

    private var exceptionCallback: ExceptionCallback? = null

    /**
     * @param bulletin 게시판 번호
     * @param pid 글 번호
     * @param onResponseCallback UI 갱신을 위한 콜백
     */
    fun fetchPost(bulletin: Int, pid: Int, onResponseCallback: (Post) -> Unit) {
        viewModelScope.launch {
            try {
                onResponseCallback(getPostUseCase(bulletin, pid))
            } catch (e: Exception) {
                Log.e("onResponseCallback", e.toString())
                exceptionCallback?.onException(e)
            }
        }
    }

    interface ExceptionCallback {
        fun onException(e: Exception)
    }

    fun registerExceptionCallback(callback: ExceptionCallback) {
        this.exceptionCallback = callback
    }
}