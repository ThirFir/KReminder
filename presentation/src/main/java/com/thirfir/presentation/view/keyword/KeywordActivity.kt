package com.thirfir.presentation.view.keyword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thirfir.presentation.databinding.ActivityKeywordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KeywordActivity : AppCompatActivity() {
    private val binding: ActivityKeywordBinding by lazy {
        ActivityKeywordBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        supportFragmentManager.beginTransaction()
            .add(binding.keywordFragmentContainer.id, KeywordFragment.newInstance())
            .commit()
    }
}