package com.example.mini_project.ui.reports

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.example.mini_project.R

/** Slice colour by rank, so the donut and its legend always match. */
object ChartPalette {
    private val colors = intArrayOf(
        R.color.food_chart,       // 1st biggest category
        R.color.bills_chart,      // 2nd
        R.color.groceries_chart,  // 3rd
        R.color.transport_chart,  // 4th
        R.color.chart_other       // everything else
    )

    const val OTHER_INDEX = 4

    @ColorInt
    fun color(context: Context, index: Int): Int =
        ContextCompat.getColor(context, colors[index.coerceIn(0, colors.lastIndex)])
}