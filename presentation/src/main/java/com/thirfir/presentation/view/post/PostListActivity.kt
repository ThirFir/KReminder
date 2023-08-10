package com.thirfir.presentation.view.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thirfir.presentation.databinding.ActivityPostListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostListActivity : AppCompatActivity(){
    private val binding: ActivityPostListBinding by lazy {
        ActivityPostListBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        supportFragmentManager.beginTransaction()
            .add(binding.postFragmentContainer.id, PostListFragment.newInstance())
            .commit()
    }

}