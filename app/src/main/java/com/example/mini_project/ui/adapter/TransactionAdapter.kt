package com.example.mini_project.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mini_project.R
import com.example.mini_project.core.Format
import com.example.mini_project.data.model.Transaction
import com.example.mini_project.databinding.ItemTransactionBinding

class TransactionAdapter(
    private val onClick: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TransactionViewHolder(
        private val binding: ItemTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tx: Transaction) {
            binding.tvBadge.text = tx.categoryEmoji
            binding.tvTitle.text = tx.note.ifBlank { tx.categoryName }
            binding.tvSubtitle.text = "${tx.categoryName} · ${Format.shortDate(tx.localDate)}"

            val sign = if (tx.isExpense) "-" else "+"
            val color = if (tx.isExpense) R.color.expense else R.color.income
            binding.tvAmount.text = sign + Format.money(tx.amount)
            binding.tvAmount.setTextColor(ContextCompat.getColor(binding.root.context, color))

            binding.root.setOnClickListener { onClick(tx) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(old: Transaction, new: Transaction) = old.id == new.id
            override fun areContentsTheSame(old: Transaction, new: Transaction) = old == new
        }
    }
}