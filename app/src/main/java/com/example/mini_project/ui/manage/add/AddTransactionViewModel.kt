package com.example.mini_project.ui.manage.add

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mini_project.MyApp
import com.example.mini_project.data.repo.CategoryRepo
import com.example.mini_project.data.repo.TransactionRepo
import com.example.mini_project.ui.manage.base.BaseManageTransactionViewModel

class AddTransactionViewModel(
    repo: TransactionRepo,
    categoryRepo: CategoryRepo
) : BaseManageTransactionViewModel(repo, categoryRepo) {

    override fun submit(amountText: String, paidWith: String, note: String) {
        val transaction = buildTransaction(amountText, paidWith, note) ?: return
        saveAndFinish { repo.insertTransaction(transaction) }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as MyApp)
                AddTransactionViewModel(app.repo, app.categoryRepo)
            }
        }
    }
}