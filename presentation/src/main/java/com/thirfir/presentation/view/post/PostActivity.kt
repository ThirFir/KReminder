package com.thirfir.presentation.view.post

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thirfir.domain.BULLETIN_QUERY
import com.thirfir.domain.PID
import com.thirfir.domain.model.PostHeader
import com.thirfir.presentation.R
import com.thirfir.presentation.databinding.ActivityPostBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.NullPointerException

@AndroidEntryPoint
class PostActivity : AppCompatActivity() {
    private val binding: ActivityPostBinding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    private val bulletin: Int by lazy {
        intent.getIntExtra(BULLETIN_QUERY, 0)
    }
    private val pid: Int by lazy {
        intent.getIntExtra(PID, 0)
    }
    private val postHeader: PostHeader by lazy {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("header", PostHeader::class.java) ?: throw NullPointerException("PostHeader is null")
        } else {
            intent.getParcelableExtra("header") ?: throw NullPointerException("PostHeader is null")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        supportFragmentManager.beginTransaction()
            .add(R.id.post_container, PostFragment.newInstance(bulletin, pid, postHeader))
            .commit()
    }
}