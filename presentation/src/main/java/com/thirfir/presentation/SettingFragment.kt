package com.thirfir.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.thirfir.presentation.adapter.SettingsAdapter
import com.thirfir.presentation.databinding.FragmentSettingBinding
import com.thirfir.presentation.model.SettingItem


class SettingFragment private constructor(): Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private val settingItems : List<SettingItem> by lazy {
        listOf(
            SettingItem(requireContext().getString(R.string.setting)) {
                // TODO : Navigate to SettingActivity(or Fragment)
            },
            SettingItem(requireContext().getString(R.string.keyword)) {
                // TODO : Navigate to KeywordActivity(or Fragment)
            },
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        binding.recyclerViewSetting.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSetting.adapter = SettingsAdapter(settingItems)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            SettingFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}