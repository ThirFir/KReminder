package com.thirfir.presentation.view.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thirfir.presentation.R
import com.thirfir.presentation.databinding.ActivitySettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {
    private val binding: ActivitySettingBinding by lazy {
        ActivitySettingBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initViews()
    }

    private fun initViews() {
        supportFragmentManager.beginTransaction()
            .add(binding.settingsFragmentContainer.id, SettingFragment.newInstance())
            .commit()
    }


}