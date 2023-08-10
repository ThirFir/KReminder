package com.thirfir.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thirfir.presentation.databinding.ActivityListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostListActivity : AppCompatActivity(){
    private val binding: ActivityListBinding by lazy {
        ActivityListBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        supportFragmentManager.beginTransaction()
            .add(binding.postFragmentContainer.id, PostFragment.newInstance())
            .commit()
    }

}