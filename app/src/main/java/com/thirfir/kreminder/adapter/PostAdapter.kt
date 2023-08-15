import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thirfir.kreminder.databinding.ItemPostBinding
import com.thirfir.kreminder.item.PostItem

class PostAdapter(
    private val onClick: (PostItem) -> Unit
) : ListAdapter<PostItem, PostAdapter.PostViewHolder>(PostItemDiffCallback()) {

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

        fun bind(item: PostItem) {
            binding.textViewSettingNum.text = item.num.toString()
            binding.textViewSettingTitle.text = item.title
            binding.root.setOnClickListener {
                binding.root.setOnClickListener {
                    onClick(item) // 클릭 이벤트 처리를 PostFragment로 전달
                }
            }
        }
    }

    fun updateData(newData: List<PostItem>) {
        submitList(newData)
    }

    private class PostItemDiffCallback : DiffUtil.ItemCallback<PostItem>() {
        override fun areItemsTheSame(oldItem: PostItem, newItem: PostItem): Boolean {
            return oldItem.num == newItem.num
        }

        override fun areContentsTheSame(oldItem: PostItem, newItem: PostItem): Boolean {
            return oldItem == newItem
        }
    }
}