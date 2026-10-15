package com.example.mini_project.ui.transactions

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

enum class TxFilter { ALL, EXPENSES, INCOME }

class TransactionsViewModel(
    private val repo: TransactionRepo
) : ViewModel() {

    private val all = MutableStateFlow<List<Transaction>>(emptyList())
    private val query = MutableStateFlow("")
    private val filter = MutableStateFlow(TxFilter.ALL)

    private val _newestFirst = MutableStateFlow(true)
    val newestFirst: StateFlow<Boolean> = _newestFirst

    /** The list after search, filter and sort. Updates whenever any of them changes. */
    val transactions: StateFlow<List<Transaction>> =
        combine(all, query, filter, _newestFirst) { list, q, f, newest ->
            val shown = list.filter { matchesFilter(it, f) && matchesQuery(it, q) }
            if (newest) shown.sortedByDescending { it.date } else shown.sortedBy { it.date }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        viewModelScope.launch {
            repo.getAllTransactions()
                .catch { it.printStackTrace() }
                .collect { all.value = it }
        }
    }

    fun setQuery(text: String) { query.value = text.trim() }

    fun setFilter(f: TxFilter) { filter.value = f }

    fun toggleSort() { _newestFirst.value = !_newestFirst.value }

    private fun matchesFilter(tx: Transaction, f: TxFilter) = when (f) {
        TxFilter.ALL -> true
        TxFilter.EXPENSES -> tx.isExpense
        TxFilter.INCOME -> !tx.isExpense
    }

    private fun matchesQuery(tx: Transaction, q: String) =
        q.isEmpty() ||
                tx.note.contains(q, ignoreCase = true) ||
                tx.categoryName.contains(q, ignoreCase = true)

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as MyApp)
                TransactionsViewModel(app.repo)
            }
        }
    }
}