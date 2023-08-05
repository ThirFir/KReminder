package com.thirfir.presentation.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thirfir.presentation.databinding.ItemPostBinding
import com.thirfir.presentation.model.PostItem


class PostAdapter(
    private var items: List<PostItem>,
    private val onClick: (PostItem) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateData(newData: List<PostItem>) {
        items = newData
        notifyDataSetChanged()
    }
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