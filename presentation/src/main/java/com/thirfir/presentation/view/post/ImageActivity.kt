package com.thirfir.presentation.view.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.thirfir.domain.SRC
import com.thirfir.presentation.R
import com.thirfir.presentation.databinding.ActivityImageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageActivity : AppCompatActivity() {
    private val binding: ActivityImageBinding by lazy {
        ActivityImageBinding.inflate(layoutInflater)
    }
    private val imageSrc : String? by lazy {
        intent.getStringExtra(SRC)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Glide.with(this)
            .load(imageSrc)
            .into(binding.imageViewContent)
    }
}