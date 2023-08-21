package com.thirfir.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thirfir.presentation.databinding.ItemOverflowMenuBinding
import com.thirfir.presentation.model.MenuItem

class OverflowMenuAdapter(
    private val menuItems: List<MenuItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class OverflowMenuViewHolder(private val binding: ItemOverflowMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: MenuItem) {
            binding.apply {
                textViewOverflowMenuItemTitle.text = menuItem.title
                root.setOnClickListener {
                    menuItem.onClick()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OverflowMenuViewHolder(
            ItemOverflowMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OverflowMenuViewHolder).bind(menuItems[position])
    }

    override fun getItemCount(): Int = menuItems.size

}