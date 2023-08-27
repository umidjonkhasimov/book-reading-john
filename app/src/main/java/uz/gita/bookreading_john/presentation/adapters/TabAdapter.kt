package uz.gita.bookreading_john.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.bookreading_john.R
import uz.gita.bookreading_john.data.model.CategoryItemData
import uz.gita.bookreading_john.databinding.ItemCategoryTabBinding

class TabAdapter : ListAdapter<CategoryItemData, TabAdapter.TabVH>(MyDiffUtil) {
    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(action: (String) -> Unit) {
        onItemClickListener = action
    }

    inner class TabVH(private val binding: ItemCategoryTabBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val newList = ArrayList<CategoryItemData>()
                    currentList.forEach { i ->
                        newList.add(i.copy(checked = false))
                    }
                    newList[adapterPosition].checked = true
                    onItemClickListener?.invoke(newList[adapterPosition].title)
                    submitList(newList)
                }
            }
        }

        fun bind(position: Int) {
            val item = getItem(position)
            binding.apply {
                root.isSelected = item.checked

                tvCategory.text = item.title
                if (item.checked) {
                    tvCategory.setTextColor(ContextCompat.getColor(root.context, R.color.white))
                } else {
                    tvCategory.setTextColor(ContextCompat.getColor(root.context, R.color.primary))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TabVH(ItemCategoryTabBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: TabVH, position: Int) = holder.bind(position)

    private object MyDiffUtil : DiffUtil.ItemCallback<CategoryItemData>() {
        override fun areItemsTheSame(oldItem: CategoryItemData, newItem: CategoryItemData) = oldItem == newItem
        override fun areContentsTheSame(oldItem: CategoryItemData, newItem: CategoryItemData) = oldItem == newItem
    }
}