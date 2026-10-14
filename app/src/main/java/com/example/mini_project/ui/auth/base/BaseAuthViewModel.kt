package com.example.mini_project.ui.auth.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseAuthViewModel : ViewModel() {

    protected val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    private val _success = MutableSharedFlow<Unit>()
    val success: SharedFlow<Unit> = _success

    protected fun showError(message: String) {
        viewModelScope.launch { _error.emit(message) }
    }

    /** Passed to AuthService; turns its callback into success / error events. */
    protected val onResult: (Boolean, String?) -> Unit = { ok, err ->
        _loading.value = false
        viewModelScope.launch {
            if (ok) _success.emit(Unit) else _error.emit(err ?: "Something went wrong")
        }
    }
}