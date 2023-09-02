package com.thirfir.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thirfir.domain.model.PostHeader
import com.thirfir.presentation.R
import com.thirfir.presentation.databinding.ItemPostHeaderBinding

class PostHeaderAdapter(
    private val onClick: (PostHeader) -> Unit
) : ListAdapter<PostHeader, PostHeaderAdapter.PostViewHolder>(PostItemDiffCallback()) {

    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(
            ItemPostHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).also { context = parent.context }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(private val binding: ItemPostHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: PostHeader) {
            binding.textViewTitle.text = item.title
            binding.textViewAuthor.text = item.author
            binding.textViewDate.text = item.date
            binding.textViewViewCount.text = "조회수 " + item.viewCount.toString()
            if(item.highlight)
                context?.getColor(R.color.secondary)
                    ?.let { binding.viewHighlight.setBackgroundColor(it) }

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