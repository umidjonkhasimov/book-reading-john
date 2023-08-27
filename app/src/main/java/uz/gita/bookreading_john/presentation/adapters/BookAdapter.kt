package uz.gita.bookreading_john.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.gita.bookreading_john.data.model.BookData
import uz.gita.bookreading_john.databinding.ItemBookBinding

class BookAdapter : ListAdapter<BookData, BookAdapter.BookVH>(MyDiffUtil) {
    private var onClickListener: ((BookData) -> Unit)? = null
    private var onLongClickListener: ((BookData) -> Unit)? = null

    fun setOnClickListener(action: (BookData) -> Unit) {
        onClickListener = action
    }

    fun setOnLongClickListener(action: (BookData) -> Unit) {
        onLongClickListener = action
    }


    inner class BookVH(private val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = getItem(position)
            binding.apply {
                root.setOnClickListener {
                    onClickListener?.invoke(getItem(adapterPosition))
                }

                root.setOnLongClickListener {
                    onLongClickListener?.invoke(item)
                    true
                }

                Glide.with(root.context)
                    .load(item.coverUrl)
                    .into(imgBook)

                tvAuthor.text = item.author
                tvTitle.text = item.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookVH =
        BookVH(ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: BookVH, position: Int) = holder.bind(position)

    private object MyDiffUtil : DiffUtil.ItemCallback<BookData>() {
        override fun areItemsTheSame(oldItem: BookData, newItem: BookData) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: BookData, newItem: BookData) = oldItem == newItem
    }
}