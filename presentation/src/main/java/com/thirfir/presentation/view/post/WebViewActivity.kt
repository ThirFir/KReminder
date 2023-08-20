package com.thirfir.presentation.view.post

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.thirfir.domain.BASE_URL
import com.thirfir.domain.BULLETIN_QUERY
import com.thirfir.domain.util.addQueryString
import com.thirfir.presentation.databinding.ActivityWebViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity : AppCompatActivity() {
    private val binding: ActivityWebViewBinding by lazy {
        ActivityWebViewBinding.inflate(layoutInflater)
    }
    private val connectUrl : String by lazy { intent.getStringExtra("url") ?: "" }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSettings()
        binding.postWebView.run {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            setDownloadListener(null) // TODO 첨부파일 다운로드

            loadUrl(connectUrl)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setSettings() {
        binding.postWebView.settings.apply {
            setSupportZoom(true)
            javaScriptEnabled = true
            // TODO 설정 추가
        }
    }
}