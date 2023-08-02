package com.thirfir.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thirfir.presentation.databinding.ItemSettingBinding
import com.thirfir.presentation.model.MenuItem

class SettingsAdapter(
    private val menuItems: List<MenuItem>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SettingViewHolder(private val binding: ItemSettingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: MenuItem) {
            binding.textViewSettingTitle.text = menuItem.title
            binding.root.setOnClickListener {
                menuItem.onClick()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        SettingViewHolder(
            ItemSettingBinding.inflate(
                LayoutInflater.from(
                parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = menuItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SettingViewHolder).bind(menuItems[position])
    }
}