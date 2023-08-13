package com.thirfir.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thirfir.domain.model.Keyword
import com.thirfir.presentation.databinding.ItemAddedKeywordBinding

class KeywordsAdapter(
    private val onDelete: (Keyword) -> Unit,
) : ListAdapter<Keyword, KeywordsAdapter.KeywordViewHolder>(KeywordDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder =
        KeywordViewHolder(
            ItemAddedKeywordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class KeywordViewHolder(
        private val binding: ItemAddedKeywordBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(keyword: Keyword) {
            binding.textViewAddedKeyword.text = keyword.name
            binding.imageViewDeleteKeyword.setOnClickListener {
                onDelete(keyword)
            }
        }
    }

    private class KeywordDiffUtil : DiffUtil.ItemCallback<Keyword>() {
        override fun areItemsTheSame(oldItem: Keyword, newItem: Keyword): Boolean = oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Keyword, newItem: Keyword): Boolean = oldItem == newItem

    }
}