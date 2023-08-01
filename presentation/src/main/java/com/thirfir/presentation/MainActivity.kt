package com.thirfir.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thirfir.presentation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        supportFragmentManager.beginTransaction()
            .add(binding.mainFragmentContainer.id, BulletinBoardFragment.newInstance())
            .commit()
    }
}