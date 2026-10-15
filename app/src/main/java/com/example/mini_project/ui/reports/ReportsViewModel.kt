package com.example.mini_project.ui.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mini_project.MyApp
import com.example.mini_project.data.model.Transaction
import com.example.mini_project.data.repo.TransactionRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.roundToInt

enum class Period { WEEK, MONTH, YEAR }

/** One piece of the donut (and one row in the legend). */
data class ChartSlice(val label: String, val value: Double, val percent: Int, val colorIndex: Int)

data class ReportState(
    val total: Double = 0.0,
    val slices: List<ChartSlice> = emptyList(),
    val dayLabels: List<String> = emptyList(),
    val dayTotals: List<Double> = emptyList()
)

class ReportsViewModel(
    private val repo: TransactionRepo
) : ViewModel() {

    private val all = MutableStateFlow<List<Transaction>>(emptyList())
    private val period = MutableStateFlow(Period.MONTH)

    /** Recalculated whenever Firestore data or the selected period changes. */
    val report: StateFlow<ReportState> =
        combine(all, period) { list, p -> build(list, p, LocalDate.now()) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, ReportState())

    init {
        viewModelScope.launch {
            repo.getAllTransactions()
                .catch { it.printStackTrace() }
                .collect { all.value = it }
        }
    }

    fun setPeriod(p: Period) { period.value = p }

    private fun build(list: List<Transaction>, p: Period, today: LocalDate): ReportState {
        val expenses = list.filter { it.isExpense && inPeriod(it.localDate, p, today) }
        val total = expenses.sumOf { it.amount }

        // Last 7 days, oldest first (for the bar chart)
        val days = (6 downTo 0).map { today.minusDays(it.toLong()) }
        val dayTotals = days.map { day ->
            list.filter { it.isExpense && it.localDate == day }.sumOf { it.amount }
        }

        return ReportState(
            total = total,
            slices = slices(expenses, total),
            dayLabels = days.map { it.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).take(1) },
            dayTotals = dayTotals
        )
    }

    private fun inPeriod(date: LocalDate, p: Period, today: LocalDate) = when (p) {
        Period.WEEK -> !date.isBefore(today.minusDays(6)) && !date.isAfter(today)
        Period.MONTH -> YearMonth.from(date) == YearMonth.from(today)
        Period.YEAR -> date.year == today.year
    }

    /** Top 4 categories by spending; the rest grouped as "Other". */
    private fun slices(expenses: List<Transaction>, total: Double): List<ChartSlice> {
        if (total <= 0.0) return emptyList()

        val ranked = expenses
            .groupBy { it.categoryName }
            .map { (name, items) -> name to items.sumOf { it.amount } }
            .sortedByDescending { it.second }

        val top = ranked.take(4).mapIndexed { index, (name, sum) ->
            ChartSlice(name, sum, percent(sum, total), index)
        }
        val rest = ranked.drop(4).sumOf { it.second }

        return if (rest > 0) {
            top + ChartSlice("Other", rest, percent(rest, total), ChartPalette.OTHER_INDEX)
        } else top
    }

    private fun percent(part: Double, total: Double) = (part / total * 100).roundToInt()

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as MyApp)
                ReportsViewModel(app.repo)
            }
        }
    }
}