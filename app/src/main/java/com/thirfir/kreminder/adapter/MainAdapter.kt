package com.thirfir.kreminder.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thirfir.kreminder.databinding.ItemMainBinding
import com.thirfir.kreminder.item.MainItem


class MainAdapter(
    private val items: List<MainItem>,
    private val onClick: (MainItem) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class MainViewHolder(private val binding: ItemMainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MainItem) {
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MainViewHolder(
            ItemMainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MainViewHolder).bind(items[position])
    }
}