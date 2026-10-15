package com.example.mini_project.ui.reports

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.example.mini_project.R
import kotlin.math.min

/** Donut chart with the total in the middle. Shows a grey ring when there is no data. */
class DonutChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }
    private val amountPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
        textSize = sp(18f)
        color = ContextCompat.getColor(context, R.color.navy)
    }
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = sp(12f)
        color = ContextCompat.getColor(context, R.color.muted)
    }
    private val bounds = RectF()

    private var slices: List<ChartSlice> = emptyList()
    private var centerText = ""
    private val caption = context.getString(R.string.spent)

    fun setData(slices: List<ChartSlice>, centerText: String) {
        this.slices = slices
        this.centerText = centerText
        invalidate()   // redraw
    }

    override fun onDraw(canvas: Canvas) {
        val stroke = dp(28f)
        val diameter = min(width, height) - stroke
        val left = (width - diameter) / 2f
        val top = (height - diameter) / 2f
        bounds.set(left, top, left + diameter, top + diameter)
        ringPaint.strokeWidth = stroke

        val total = slices.sumOf { it.value }
        if (total <= 0.0) {
            ringPaint.color = ContextCompat.getColor(context, R.color.border)
            canvas.drawArc(bounds, 0f, 360f, false, ringPaint)
        } else {
            var start = -90f   // start at 12 o'clock
            slices.forEach { slice ->
                val sweep = (slice.value / total * 360.0).toFloat()
                ringPaint.color = ChartPalette.color(context, slice.colorIndex)
                canvas.drawArc(bounds, start, sweep, false, ringPaint)
                start += sweep
            }
        }

        val cx = width / 2f
        val cy = height / 2f
        canvas.drawText(centerText, cx, cy, amountPaint)
        canvas.drawText(caption, cx, cy + dp(18f), labelPaint)
    }

    private fun dp(v: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics)
    private fun sp(v: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, resources.displayMetrics)
}