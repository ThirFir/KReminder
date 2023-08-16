package com.thirfir.presentation.view.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thirfir.presentation.databinding.FragmentNotificationSettingBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotificationSettingFragment: Fragment() {
    private lateinit var binding: FragmentNotificationSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
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