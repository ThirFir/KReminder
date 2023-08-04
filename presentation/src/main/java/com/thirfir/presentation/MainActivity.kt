package com.thirfir.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thirfir.presentation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
