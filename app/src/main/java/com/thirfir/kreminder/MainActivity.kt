package com.thirfir.kreminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thirfir.kreminder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        supportFragmentManager.beginTransaction()
            .add(binding.mainFragmentContainer.id, PostFragment.newInstance())
            .commit()
    }
}