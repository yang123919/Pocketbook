package com.example.mini_project.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mini_project.databinding.ItemLegendBinding
import com.example.mini_project.ui.reports.ChartPalette
import com.example.mini_project.ui.reports.ChartSlice

class LegendAdapter : ListAdapter<ChartSlice, LegendAdapter.LegendViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegendViewHolder {
        val binding = ItemLegendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LegendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LegendViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LegendViewHolder(
        private val binding: ItemLegendBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(slice: ChartSlice) {
            val color = ChartPalette.color(binding.root.context, slice.colorIndex)
            binding.viewDot.backgroundTintList = ColorStateList.valueOf(color)
            binding.tvLabel.text = slice.label
            binding.tvPercent.text = "${slice.percent}%"
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ChartSlice>() {
            override fun areItemsTheSame(old: ChartSlice, new: ChartSlice) = old.label == new.label
            override fun areContentsTheSame(old: ChartSlice, new: ChartSlice) = old == new
        }
    }
}