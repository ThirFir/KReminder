package com.thirfir.presentation

import android.content.Context
import android.content.Intent
import com.thirfir.domain.BASE_URL
import com.thirfir.domain.BULLETIN_QUERY
import com.thirfir.domain.PID_QUERY
import com.thirfir.domain.util.addQueryString
import com.thirfir.presentation.view.post.WebViewActivity


fun Context.openPostUsingWebView(bulletin: Int, pid: Int) {
    val intent = Intent(this, WebViewActivity::class.java).apply {
        putExtra("url", BASE_URL.addQueryString(BULLETIN_QUERY, bulletin).addQueryString(PID_QUERY, pid))
    }
    startActivity(intent)
}