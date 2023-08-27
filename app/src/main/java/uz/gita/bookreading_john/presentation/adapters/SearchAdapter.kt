package uz.gita.bookreading_john.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.gita.bookreading_john.data.model.BookData
import uz.gita.bookreading_john.databinding.ItemSearchBookBinding

class SearchAdapter : ListAdapter<BookData, SearchAdapter.SearchVH>(MyDiffUtil) {
    private var onItemClickListener: ((BookData) -> Unit)? = null

    fun setOnItemClickListener(action: (BookData) -> Unit) {
        onItemClickListener = action
    }

    inner class SearchVH(private val binding: ItemSearchBookBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(getItem(adapterPosition))
            }
        }

        fun bind(position: Int) {
            val item = getItem(position)
            binding.apply {
                Glide.with(root.context)
                    .load(item.coverUrl)
                    .into(imgBookSearch)

                tvTitleSearch.text = item.title
                tvAuthorSearch.text = "Author: ${item.author}"
                tvRateSearch.text = "Rating:  ${item.rate}"
                tvGenreSearch.text = "Genre: ${item.genre}"
                tvYearSearch.text = "Year: ${item.year}"
                tvPageSearch.text = "Pages: ${item.page}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVH =
        SearchVH(ItemSearchBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: SearchVH, position: Int) = holder.bind(position)

    private object MyDiffUtil : DiffUtil.ItemCallback<BookData>() {
        override fun areItemsTheSame(oldItem: BookData, newItem: BookData) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: BookData, newItem: BookData) = oldItem == newItem
    }
}