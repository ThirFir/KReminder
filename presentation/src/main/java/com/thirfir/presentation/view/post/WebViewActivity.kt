package com.thirfir.presentation.view.post

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.webkit.CookieManager
import android.webkit.DownloadListener
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
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
    private var downloadId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSettings()
        binding.postWebView.run {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
                download(url, userAgent, contentDisposition, mimetype, contentLength)
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

    private fun download(url: String, userAgent: String, contentDisposition: String, mimetype: String, contentLength: Long) {
        val downloadPermissions = listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            downloadPermissions.forEach {
                if(ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, downloadPermissions.toTypedArray(), 0)
                }
            }
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) {
                it.forEach { perm ->
                    if (!perm.value) {
                        Toast.makeText(this, "다운로드 권한을 승인해주세요", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }.launch(downloadPermissions.toTypedArray())
        }

        try {
            Toast.makeText(this@WebViewActivity, "파일 다운로드 중...", Toast.LENGTH_SHORT).show()

            val filename = URLUtil.guessFileName(url, contentDisposition, mimetype)
            val cookies = CookieManager.getInstance().getCookie(url)

            DownloadManager.Request(Uri.parse(url)).apply {
                addRequestHeader("cookie", cookies)
                addRequestHeader("User-Agent", userAgent)
                setDescription("파일 다운로드 중...")
                setTitle(filename)

                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)

            }.also {
                registerDownloadReceiver(getSystemService(DOWNLOAD_SERVICE) as DownloadManager)
                val dManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                downloadId = dManager.enqueue(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        finish()
    }

    private fun registerDownloadReceiver(downloadManager: DownloadManager) {
        val downloadReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) ?: -1

                when (intent?.action) {
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE -> {

                        if(downloadId == id){
                            val query: DownloadManager.Query = DownloadManager.Query()
                            query.setFilterById(id)
                            val cursor = downloadManager.query(query)
                            if (!cursor.moveToFirst()) {
                                return
                            }

                            val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                            val status = cursor.getInt(columnIndex)
                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                Toast.makeText(context, "파일이 다운로드 되었어요", Toast.LENGTH_SHORT).show()
                            } else if (status == DownloadManager.STATUS_FAILED) {
                                Toast.makeText(context, "파일 다운로드를 실패했어요", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        IntentFilter().run {
            addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            registerReceiver(downloadReceiver, this)
        }
    }
}