package com.thirfir.presentation.model

import android.content.Context
import android.content.Intent
import com.thirfir.domain.BULLETIN_QUERY
import com.thirfir.presentation.view.post.PostListActivity

enum class BulletinBoardItem(
    val title: String,
    val bulletin: Int
) {
    GENERAL("일반공지", 14),
    SCHOLARSHIP("장학공지", 15),
    ACADEMIC("학사공지", 16),
    STUDENT_LIFE("학생생활", 21),
    RECRUITMENT("채용공지", 150),
    FIELD_PRACTICE("현장실습공지", 151),
    SOCIAL_SERVICE("사회봉사공지", 191),
    FREE("자유게시판", 22);
    fun onClick(context: Context) {
        val intent = Intent(context, PostListActivity::class.java).apply {
            putExtra(BULLETIN_QUERY, this@BulletinBoardItem.bulletin)
            putExtra("title", this@BulletinBoardItem.title)
        }
        context.startActivity(intent)
    }
}