package com.thirfir.presentation.view.post

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.thirfir.domain.BASE_URL
import com.thirfir.domain.BULLETIN_QUERY
import com.thirfir.domain.PID
import com.thirfir.domain.PID_QUERY
import com.thirfir.domain.model.Bookmark
import com.thirfir.domain.model.PostHeader
import com.thirfir.domain.util.addQueryString
import com.thirfir.presentation.R
import com.thirfir.presentation.databinding.ActivityPostBinding
import com.thirfir.presentation.openPostUsingWebView
import com.thirfir.presentation.viewmodel.BookmarksViewModel
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
    private val bulletinTitle: String by lazy {
        intent.getStringExtra("title") ?: ""
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

    private val bookmarksViewModel: BookmarksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        initClickListeners()
    }

    private fun initViews() {
        supportFragmentManager.beginTransaction()
            .add(R.id.post_container, PostFragment.newInstance(bulletin, pid, postHeader))
            .commit()
        changeBookmarkIcon(bookmarksViewModel.isBookmarked(pid))
        binding.topAppBar.title = bulletinTitle
    }

    private fun initClickListeners() {
        binding.topAppBar.run {
            setNavigationOnClickListener {
                finish()
            }

            setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.register_bookmark -> {
                        bookmarksViewModel.toggleBookmark(
                            Bookmark(
                                pid = pid,
                                title = postHeader.title,
                                category = postHeader.category,
                                timestamp = System.currentTimeMillis(),
                                bulletin = bulletin
                            )
                        ) { bookmarked ->
                            changeBookmarkIcon(bookmarked)
                            showBookmarkingMessage(bookmarked)
                        }
                        true
                    }
                    R.id.external_link -> {
                        openPostUsingWebView(bulletin, pid)
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun changeBookmarkIcon(bookmarked: Boolean) {
        if(bookmarked) {
            binding.topAppBar.menu.findItem(R.id.register_bookmark).setIcon(R.drawable.ic_star_color)
        } else {
            binding.topAppBar.menu.findItem(R.id.register_bookmark).setIcon(R.drawable.ic_star)
        }
    }

    private fun showBookmarkingMessage(bookmarked: Boolean) {
        if(bookmarked) {
            Snackbar.make(binding.root, "북마크 등록되었어요.", Snackbar.LENGTH_SHORT).show()
        } else {
            Snackbar.make(binding.root, "북마크 해제되었어요.", Snackbar.LENGTH_SHORT).show()
        }
    }
}