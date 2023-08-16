package com.thirfir.presentation.view.post

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.thirfir.domain.BASE_URL
import com.thirfir.domain.util.addQueryString
import com.thirfir.presentation.databinding.ActivityPostWebViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostWebViewActivity : AppCompatActivity() {
    private val binding: ActivityPostWebViewBinding by lazy {
        ActivityPostWebViewBinding.inflate(layoutInflater)
    }
    private val bulletin : Int by lazy { intent.getIntExtra(BULLETIN, 0) }
    private val pid : Int by lazy { intent.getIntExtra(PID, 0) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSettings()
        binding.postWebView.run {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            setDownloadListener(null) // TODO 첨부파일 다운로드

            loadUrl(BASE_URL.addQueryString("b", bulletin).addQueryString("p", pid))
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
    companion object {
        private const val BULLETIN = "bulletin"
        private const val PID = "pid"
    }
}