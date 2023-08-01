package com.thirfir.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thirfir.presentation.databinding.ItemBulletinBoardBinding
import com.thirfir.presentation.model.BulletinBoard

class BulletinBoardAdapter(private val boards: List<BulletinBoard>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class BulletinBoardViewHolder(private val binding: ItemBulletinBoardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(board: BulletinBoard) {
            binding.textViewBulletinBoardTitle.text = board.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        BulletinBoardViewHolder(ItemBulletinBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = boards.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BulletinBoardViewHolder).bind(boards[position])
    }
}