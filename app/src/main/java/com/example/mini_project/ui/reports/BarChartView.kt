package com.example.mini_project.ui.reports

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.example.mini_project.R
import kotlin.math.max

/** One bar per day; the highest day is highlighted teal. */
class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = sp(11f)
        color = ContextCompat.getColor(context, R.color.muted)
    }
    private val highlight = ContextCompat.getColor(context, R.color.teal)
    private val normal = ContextCompat.getColor(context, R.color.teal_soft)
    private val barRect = RectF()

    private var labels: List<String> = emptyList()
    private var values: List<Double> = emptyList()

    fun setData(labels: List<String>, values: List<Double>) {
        this.labels = labels
        this.values = values
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (values.isEmpty()) return

        val labelSpace = sp(11f) + dp(8f)
        val chartHeight = height - labelSpace
        val peak = max(values.max(), 1.0)
        val slot = width.toFloat() / values.size
        val gap = dp(8f)
        val radius = dp(6f)

        values.forEachIndexed { i, value ->
            val barHeight = max((value / peak * chartHeight * 0.95).toFloat(), dp(4f))
            val left = i * slot + gap / 2f
            barRect.set(left, chartHeight - barHeight, left + slot - gap, chartHeight)
            barPaint.color = if (value > 0 && value == peak) highlight else normal
            canvas.drawRoundRect(barRect, radius, radius, barPaint)
            canvas.drawText(labels.getOrElse(i) { "" }, i * slot + slot / 2f, height - dp(2f), labelPaint)
        }
    }

    private fun dp(v: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics)
    private fun sp(v: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, resources.displayMetrics)
}