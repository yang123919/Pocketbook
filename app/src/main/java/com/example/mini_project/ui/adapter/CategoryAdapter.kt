package com.example.mini_project.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mini_project.data.model.Category
import com.example.mini_project.databinding.ItemCategoryBinding

data class CategoryItem(val category: Category, val selected: Boolean)

class CategoryAdapter(
    private val onClick: (Category) -> Unit
) : ListAdapter<CategoryItem, CategoryAdapter.CategoryViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryItem) {
            binding.tvEmoji.text = item.category.emoji
            binding.tvLabel.text = item.category.name
            binding.root.isSelected = item.selected   // teal outline from bg_category_cell
            binding.root.setOnClickListener { onClick(item.category) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CategoryItem>() {
            override fun areItemsTheSame(old: CategoryItem, new: CategoryItem) =
                old.category.id == new.category.id
            override fun areContentsTheSame(old: CategoryItem, new: CategoryItem) = old == new
        }
    }
}