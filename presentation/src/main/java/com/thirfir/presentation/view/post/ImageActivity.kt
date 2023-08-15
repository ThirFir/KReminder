package com.thirfir.presentation.view.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thirfir.presentation.R
import com.thirfir.presentation.databinding.ActivityImageBinding

class ImageActivity : AppCompatActivity() {
    private val binding: ActivityImageBinding by lazy {
        ActivityImageBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}