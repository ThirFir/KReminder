package com.thirfir.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thirfir.domain.model.AttachedFile
import com.thirfir.presentation.databinding.ItemAttachedFileBinding

class AttachedFileAdapter(
    private val onClick: (AttachedFile) -> Unit,
) : ListAdapter<AttachedFile, AttachedFileAdapter.AttachedFileViewHolder>(AttachedFileDiffUtil()) {
    override fun onBindViewHolder(holder: AttachedFileViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachedFileViewHolder =
        AttachedFileViewHolder(
            ItemAttachedFileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = currentList.size

    inner class AttachedFileViewHolder(
        private val binding: ItemAttachedFileBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(attachedFile: AttachedFile) {
            binding.textAttachedFileName.text = attachedFile.name
            binding.root.setOnClickListener {
                onClick(attachedFile)
            }
        }
    }

    private class AttachedFileDiffUtil : DiffUtil.ItemCallback<AttachedFile>() {
        override fun areItemsTheSame(oldItem: AttachedFile, newItem: AttachedFile): Boolean = oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: AttachedFile, newItem: AttachedFile): Boolean = oldItem == newItem

    }
}