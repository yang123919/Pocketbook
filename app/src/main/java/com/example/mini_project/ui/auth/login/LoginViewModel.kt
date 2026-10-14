package com.example.mini_project.ui.auth.login

import android.util.Patterns
import com.example.mini_project.core.AuthService
import com.example.mini_project.ui.auth.base.BaseAuthViewModel

class LoginViewModel : BaseAuthViewModel() {

    fun login(email: String, pass: String) {
        when {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> showError("Enter a valid email")
            pass.isEmpty() -> showError("Enter your password")
            else -> {
                _loading.value = true
                AuthService.login(email, pass, onResult)
            }
        }
    }
}