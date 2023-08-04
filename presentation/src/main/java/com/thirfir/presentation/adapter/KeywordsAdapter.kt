package com.thirfir.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thirfir.domain.model.Keyword
import com.thirfir.presentation.databinding.ItemAddedKeywordBinding

class KeywordsAdapter(
    private val keywords: List<Keyword>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        KeywordViewHolder(
            ItemAddedKeywordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = keywords.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as KeywordViewHolder).bind(keywords[position])
    }

    inner class KeywordViewHolder(
        private val binding: ItemAddedKeywordBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(keyword: Keyword) {
            binding.textViewAddedKeyword.text = keyword.name
        }
    }
}