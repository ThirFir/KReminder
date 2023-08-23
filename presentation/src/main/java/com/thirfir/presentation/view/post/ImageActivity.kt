package com.thirfir.presentation.view.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
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
    private var doubleTapGestureDetector: GestureDetector? = null
    private lateinit var gestureDetector: GestureDetector
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var initialTranslateX = 0f
    private var initialTranslateY = 0f
    private var scaleFactor = 1.0f
    private var doubleTap = false

    private val imageSrc: String? by lazy {
        intent.getStringExtra(SRC)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gestureDetector = GestureDetector(this, GestureListener())//더블클릭
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())//확대축소
        
        Glide.with(this)
            .load(imageSrc)
            .into(binding.imageViewContent)
    }


    override fun onTouchEvent(event: android.view.MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        scaleGestureDetector?.onTouchEvent(event)

        when (event.action) {//이미지 이동 처리 로직
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = event.x
                lastTouchY = event.y
                initialTranslateX = binding.imageViewContent.translationX
                initialTranslateY = binding.imageViewContent.translationY
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = event.x - lastTouchX
                val deltaY = event.y - lastTouchY

                binding.imageViewContent.translationX = initialTranslateX + deltaX
                binding.imageViewContent.translationY = initialTranslateY + deltaY
            }
        }

        doubleTapGestureDetector?.onTouchEvent(event)
        return true
    }


    //더블클릭
    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            if (doubleTap) {
                scaleFactor = 1.0f
                binding.imageViewContent.scaleX = scaleFactor
                binding.imageViewContent.scaleY = scaleFactor
                binding.imageViewContent.translationX=0f
                binding.imageViewContent.translationY = 0f

            } else {
                scaleFactor = 2.0f
                binding.imageViewContent.scaleX = scaleFactor
                binding.imageViewContent.scaleY = scaleFactor
            }
            doubleTap = !doubleTap
            return true
        }
    }

   //제스처 이벤트를 처리하는 클래스(확대 축소)
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