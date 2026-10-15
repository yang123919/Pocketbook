package com.example.mini_project.ui.manage.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mini_project.core.toTimestamp
import com.example.mini_project.data.model.Category
import com.example.mini_project.data.model.Transaction
import com.example.mini_project.data.model.TxType
import com.example.mini_project.data.repo.CategoryRepo
import com.example.mini_project.data.repo.TransactionRepo
import com.example.mini_project.ui.adapter.CategoryItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class FormState(
    val type: String = TxType.EXPENSE,
    val categoryId: String? = null,
    val date: LocalDate = LocalDate.now()
)

abstract class BaseManageTransactionViewModel(
    protected val repo: TransactionRepo,
    categoryRepo: CategoryRepo
) : ViewModel() {

    protected val _form = MutableStateFlow(FormState())
    val form: StateFlow<FormState> = _form

    /** Live from Firestore: users/{uid}/categories */
    private val categories = MutableStateFlow<List<Category>>(emptyList())

    /** Categories for the selected type, with the selected one marked. */
    val categoryItems: StateFlow<List<CategoryItem>> =
        combine(categories, _form) { list, form ->
            list.filter { it.type == form.type }
                .map { CategoryItem(it, it.id == form.categoryId) }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _amountError = MutableStateFlow<String?>(null)
    val amountError: StateFlow<String?> = _amountError

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    protected val _finish = MutableSharedFlow<Unit>()
    val finish: SharedFlow<Unit> = _finish

    init {
        viewModelScope.launch {
            categoryRepo.getCategories()
                .catch { showError(it.message ?: "Could not load categories") }
                .collect { list ->
                    categories.value = list
                    keepValidSelection()
                }
        }
    }

    fun setType(type: String) {
        if (type == _form.value.type) return
        _form.update { it.copy(type = type, categoryId = firstCategoryId(type)) }
    }

    fun setCategory(category: Category) {
        _form.update { it.copy(categoryId = category.id) }
    }

    fun setDate(date: LocalDate) {
        _form.update { it.copy(date = date) }
    }

    abstract fun submit(amountText: String, paidWith: String, note: String)

    protected fun buildTransaction(amountText: String, paidWith: String, note: String): Transaction? {
        val amount = amountText.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _amountError.value = "Enter an amount above 0"
            return null
        }
        _amountError.value = null

        val form = _form.value
        val category = categories.value.firstOrNull { it.id == form.categoryId }
        if (category == null) {
            showError("Choose a category")
            return null
        }

        return Transaction(
            amount = amount,
            type = form.type,
            categoryId = category.id.orEmpty(),
            categoryName = category.name,
            categoryEmoji = category.emoji,
            date = form.date.toTimestamp(),
            paidWith = paidWith.ifBlank { "Cash" },
            note = note
        )
    }

    protected fun saveAndFinish(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
                _finish.emit(Unit)
            } catch (e: Exception) {
                _error.emit(e.message ?: "Could not save. Please try again.")
            }
        }
    }

    protected fun showError(message: String) {
        viewModelScope.launch { _error.emit(message) }
    }

    private fun firstCategoryId(type: String): String? =
        categories.value.firstOrNull { it.type == type }?.id

    /** If nothing (or a deleted category) is selected, pick the first one of this type. */
    private fun keepValidSelection() {
        val form = _form.value
        val valid = categories.value.any { it.id == form.categoryId && it.type == form.type }
        if (!valid) _form.update { it.copy(categoryId = firstCategoryId(it.type)) }
    }
}