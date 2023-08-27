package uz.gita.bookreading_john.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.bookreading_john.data.model.BookData
import uz.gita.bookreading_john.data.model.CategoryData
import uz.gita.bookreading_john.databinding.ItemCategoryBinding

class HomeAdapter : ListAdapter<CategoryData, HomeAdapter.HomeVH>(MyDiffUtil) {
    private var onItemClickListener: ((BookData) -> Unit)? = null

    fun setOnItemClickListener(action: (BookData) -> Unit) {
        onItemClickListener = action
    }

    inner class HomeVH(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = getItem(position)

            binding.apply {
                tvCategory.text = item.title
                val bookAdapter = BookAdapter()
                bookAdapter.submitList(item.books)
                bookAdapter.setOnClickListener { onItemClickListener?.invoke(it) }

                rvHorizontal.adapter = bookAdapter
                rvHorizontal.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeVH =
        HomeVH(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: HomeVH, position: Int) = holder.bind(position)

    private object MyDiffUtil : DiffUtil.ItemCallback<CategoryData>() {
        override fun areItemsTheSame(oldItem: CategoryData, newItem: CategoryData) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: CategoryData, newItem: CategoryData) = oldItem == newItem
    }
}