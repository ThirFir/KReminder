package com.thirfir.presentation.view.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ScaleGestureDetector
import com.bumptech.glide.Glide
import com.thirfir.domain.SRC
import com.thirfir.presentation.R
import com.thirfir.presentation.databinding.ActivityImageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageBinding
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f

    private val imageSrc: String? by lazy {
        intent.getStringExtra(SRC)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        Glide.with(this)
            .load(imageSrc)
            .into(binding.imageViewContent)
    }

    override fun onTouchEvent(event: android.view.MotionEvent): Boolean {
        scaleGestureDetector?.onTouchEvent(event)
        return true
    }

    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f))
            binding.imageViewContent.scaleX = scaleFactor
            binding.imageViewContent.scaleY = scaleFactor
            return true
        }
    }
}