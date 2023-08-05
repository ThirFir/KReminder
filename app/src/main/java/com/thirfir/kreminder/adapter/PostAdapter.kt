package com.thirfir.kreminder.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thirfir.kreminder.databinding.ItemPostBinding
import com.thirfir.kreminder.item.PostItem


class PostAdapter(
    private val items: List<PostItem>,
    private val onClick: (PostItem) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class MainViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostItem) {
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MainViewHolder(
            ItemPostBinding.inflate(
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