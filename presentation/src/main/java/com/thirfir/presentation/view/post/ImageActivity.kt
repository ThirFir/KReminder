package com.thirfir.presentation.view.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var initialTranslateX = 0f
    private var initialTranslateY = 0f
    override fun onTouchEvent(event: android.view.MotionEvent): Boolean {
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

        //    gestureDetector?.onTouchEvent(event)//더블클릭메서드호출
        return true
    }

   /* val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean { // 더블클릭 이벤트 처리 로직
            if (scaleFactor > 1.0f) {
                scaleFactor = 1.0f // 이미지를 원래 크기로 되돌립니다.

            } else {
                scaleFactor = 2.0f // 이미지를 2배 확대합니다.

            }
            binding.imageViewContent.scaleX = scaleFactor
            binding.imageViewContent.scaleY = scaleFactor
            return true
        }
    })
*/
   //제스처 이벤트를 처리하는 클래스
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