package com.thirfir.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thirfir.domain.model.Bookmark
import com.thirfir.domain.usecase.BookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val bookmarkUseCase: BookmarkUseCase
) : ViewModel() {

    private val _bookmarks : MutableLiveData<List<Bookmark>> = MutableLiveData()
    val bookmarks : LiveData<List<Bookmark>> get() = _bookmarks

    init {
        getBookmarks()
    }

    fun getBookmarks() {
        viewModelScope.launch {
            _bookmarks.value = bookmarkUseCase.getBookmarks()
        }
    }

    fun insertBookmark(bookmark: Bookmark) {
        bookmarkUseCase.insertBookmark(bookmark) {
            _bookmarks.postValue(_bookmarks.value?.plus(it))
        }
    }

    fun deleteBookmark(pid: Int) {
        bookmarkUseCase.deleteBookmark(pid) {
            _bookmarks.postValue(_bookmarks.value?.filter { it.pid != pid })
        }
    }

    fun toggleBookmark(bookmark: Bookmark, onComplete: (Boolean) -> Unit) {
        if(isBookmarked(bookmark.pid)) {
            deleteBookmark(bookmark.pid)
            onComplete(false)
        } else {
            insertBookmark(bookmark)
            onComplete(true)
        }
    }

    fun isBookmarked(pid: Int) : Boolean {
        return _bookmarks.value?.any { it.pid == pid } ?: false
    }
}