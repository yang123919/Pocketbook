package com.example.mini_project.ui.manage.edit

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mini_project.MyApp
import com.example.mini_project.data.model.Transaction
import com.example.mini_project.data.repo.CategoryRepo
import com.example.mini_project.data.repo.TransactionRepo
import com.example.mini_project.ui.manage.base.BaseManageTransactionViewModel
import com.example.mini_project.ui.manage.base.FormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditTransactionViewModel(
    repo: TransactionRepo,
    categoryRepo: CategoryRepo
) : BaseManageTransactionViewModel(repo, categoryRepo) {

    private var transactionId = ""

    /** The saved transaction, used to fill the text boxes once loaded. */
    private val _transaction = MutableStateFlow<Transaction?>(null)
    val transaction: StateFlow<Transaction?> = _transaction

    fun load(id: String) {
        if (id == transactionId) return      // already loaded (e.g. after rotation)
        transactionId = id
        viewModelScope.launch {
            try {
                val tx = repo.getTransaction(id)
                if (tx == null) {
                    showError("Transaction not found")
                    return@launch
                }
                _form.value = FormState(tx.type, tx.categoryId, tx.localDate)
                _transaction.value = tx
            } catch (e: Exception) {
                showError(e.message ?: "Could not load transaction")
            }
        }
    }

    override fun submit(amountText: String, paidWith: String, note: String) {
        val transaction = buildTransaction(amountText, paidWith, note) ?: return
        saveAndFinish { repo.updateTransaction(transactionId, transaction) }
    }

    fun delete() {
        saveAndFinish { repo.deleteTransaction(transactionId) }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as MyApp)
                EditTransactionViewModel(app.repo, app.categoryRepo)
            }
        }
    }
}