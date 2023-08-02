package com.thirfir.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thirfir.presentation.adapter.OverflowMenuAdapter
import com.thirfir.presentation.databinding.ModalBottomSheetOverflowMenuBinding
import com.thirfir.presentation.model.MenuItem

class OverflowMenuModalBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: ModalBottomSheetOverflowMenuBinding
    private val menuItems: List<MenuItem> by lazy {
        listOf(
            MenuItem(
                icon = R.drawable.ic_settings_36,
                title = requireContext().getString(R.string.setting)
            ) {
                requireContext().startActivity(
                    Intent(requireContext(), SettingActivity::class.java)
                )
                dismiss()
            },
            MenuItem(
                icon = R.drawable.ic_star_36,
                title = requireContext().getString(R.string.watchlist)) {
                // TODO : Navigate to WatchlistActivity(or Fragment)
                dismiss()
            },
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ModalBottomSheetOverflowMenuBinding.inflate(layoutInflater, container, false)

        binding.recyclerViewOverflowMenu.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.recyclerViewOverflowMenu.adapter = OverflowMenuAdapter(menuItems)
        return binding.root
    }

    companion object {
        const val TAG = "OverflowMenuModalBottomSheet"
    }
}