package com.thirfir.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thirfir.presentation.databinding.ItemSettingBinding
import com.thirfir.presentation.model.SettingItem

class SettingsAdapter(
    private val settings: List<SettingItem>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SettingViewHolder(private val binding: ItemSettingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(setting: SettingItem) {
            binding.textViewSettingTitle.text = setting.title
            binding.root.setOnClickListener {
                setting.onClick()
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

    override fun getItemCount(): Int = settings.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SettingViewHolder).bind(settings[position])
    }
}