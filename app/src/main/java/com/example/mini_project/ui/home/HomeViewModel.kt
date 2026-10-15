package com.example.mini_project.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mini_project.MyApp
import com.example.mini_project.core.AuthService
import com.example.mini_project.data.model.Transaction
import com.example.mini_project.data.repo.TransactionRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

/** Numbers for the navy balance card. */
data class HomeSummary(
    val monthName: String = "",
    val balance: Double = 0.0,
    val income: Double = 0.0,
    val spent: Double = 0.0,
    val progressPercent: Int = 0
)

class HomeViewModel(
    private val repo: TransactionRepo
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())

    /** Newest 5 for the "Recent" card. */
    val recent: StateFlow<List<Transaction>> = _transactions
        .map { it.take(5) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val summary: StateFlow<HomeSummary> = _transactions
        .map { summarise(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, HomeSummary())

    val userName: String get() = AuthService.displayName

    val greeting: String
        get() = when (LocalTime.now().hour) {
            in 5..11 -> "Good morning"
            in 12..17 -> "Good afternoon"
            else -> "Good evening"
        }

    init {
        viewModelScope.launch {
            repo.getAllTransactions()
                .catch { it.printStackTrace() }   // e.g. after logout
                .collect { _transactions.value = it }
        }
    }

    fun logout() = AuthService.logout()

    private fun summarise(all: List<Transaction>): HomeSummary {
        val month = YearMonth.now()
        val thisMonth = all.filter { YearMonth.from(it.localDate) == month }
        val income = thisMonth.filterNot { it.isExpense }.sumOf { it.amount }
        val spent = thisMonth.filter { it.isExpense }.sumOf { it.amount }
        return HomeSummary(
            monthName = month.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            balance = income - spent,
            income = income,
            spent = spent,
            progressPercent = if (income > 0) (spent / income * 100).toInt().coerceIn(0, 100) else 0
        )
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as MyApp)
                HomeViewModel(app.repo)
            }
        }
    }
}