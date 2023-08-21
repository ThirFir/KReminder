package com.thirfir.presentation.view.post

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.thirfir.domain.BASE_URL
import com.thirfir.domain.BULLETIN_QUERY
import com.thirfir.domain.util.addQueryString
import com.thirfir.presentation.databinding.ActivityWebViewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder

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
            setDownloadListener { url, _, _, _, _ ->

            }

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

    private fun download(downloadUrl: String, contentDisposition: String, mimeType: String, contentLength: Long) {
        try {
            val request = DownloadManager.Request(Uri.parse(downloadUrl))
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

            URLDecoder.decode(contentDisposition, "UTF-8").split(";").forEach {
                val split = it.split("=")
                if(split[0].trim() == "filename") {
                    request.setDestinationInExternalPublicDir(
                        "/Download",
                        split[1].replace("\"", "")
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}