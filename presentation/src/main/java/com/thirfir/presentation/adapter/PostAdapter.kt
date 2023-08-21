package com.thirfir.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thirfir.domain.model.PostHeader
import com.thirfir.presentation.databinding.ItemPostBinding

class PostAdapter(
    private val postHeaders: List<PostHeader>,
    private val onClick: (PostHeader) -> Unit
) : ListAdapter<PostHeader, PostAdapter.PostViewHolder>(PostItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PostHeader) {
            binding.textViewSettingNum.text = item.pid.toString()
            binding.textViewSettingTitle.text = item.title
            binding.root.setOnClickListener {
                onClick(item) // 클릭 이벤트 처리를 PostFragment로 전달
            }

        }
    }

    fun updateData(newData: List<PostHeader>) {
        submitList(newData)
    }

    private class PostItemDiffCallback : DiffUtil.ItemCallback<PostHeader>() {
        override fun areItemsTheSame(oldItem: PostHeader, newItem: PostHeader): Boolean {
            return oldItem.pid == newItem.pid
        }

        override fun areContentsTheSame(oldItem: PostHeader, newItem: PostHeader): Boolean {
            return oldItem == newItem
        }
    }
}