package com.thirfir.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thirfir.presentation.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private val binding: ActivitySettingBinding by lazy {
        ActivitySettingBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
    }


}