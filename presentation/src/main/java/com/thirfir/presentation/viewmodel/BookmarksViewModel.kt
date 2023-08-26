package com.thirfir.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thirfir.domain.model.Bookmark
import com.thirfir.domain.usecase.GetBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val getBookmarkUseCase: GetBookmarkUseCase
) : ViewModel() {

    private val _bookmarks : MutableLiveData<List<Bookmark>> = MutableLiveData()
    val bookmarks : LiveData<List<Bookmark>> get() = _bookmarks

    private fun getBookmarks() {
        viewModelScope.launch {
            _bookmarks.value = getBookmarkUseCase.getBookmarks()
        }
    }

    private fun insertBookmark(bookmark: Bookmark) {
        getBookmarkUseCase.insertBookmark(bookmark) {
            _bookmarks.postValue(_bookmarks.value?.plus(it))
        }
    }

    private fun deleteBookmark(pid: Int) {
        getBookmarkUseCase.deleteBookmark(pid) {
            _bookmarks.postValue(_bookmarks.value?.filter { it.pid != pid })
        }
    }
}