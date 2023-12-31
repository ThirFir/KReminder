package com.thirfir.presentation.view.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.thirfir.presentation.R
import com.thirfir.presentation.adapter.SettingsAdapter
import com.thirfir.presentation.databinding.FragmentSettingBinding
import com.thirfir.presentation.model.MenuItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private val menuItems: List<MenuItem> by lazy {
        listOf(
            MenuItem(title = requireContext().getString(R.string.notification)) {
                // TODO : Navigate to NotificationActivity(or Fragment)
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.settings_fragment_container,
                        NotificationSettingFragment.newInstance()
                    )
                    .addToBackStack(null)
                    .commit()
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
        initClickListeners()
        initRecyclerView()
        return binding.root
    }

    private fun initClickListeners() {
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().finish()
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewSetting.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSetting.adapter = SettingsAdapter(menuItems)
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