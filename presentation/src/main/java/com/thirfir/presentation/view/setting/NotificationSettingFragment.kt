package com.thirfir.presentation.view.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.thirfir.presentation.databinding.FragmentNotificationSettingBinding
import com.thirfir.presentation.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotificationSettingFragment: Fragment() {
    private lateinit var binding: FragmentNotificationSettingBinding

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationSettingBinding.inflate(layoutInflater, container, false)
        initView()
        initEvent()
        return binding.root
    }

    private fun initView() {
        binding.viewModel = viewModel
    }

    private fun initEvent() {
        binding.switchNotificationState.setOnClickListener { _ ->
            viewModel.updateSettings {
                it.allowNotification = !it.allowNotification
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            NotificationSettingFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}