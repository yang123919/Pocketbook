package com.example.mini_project.ui.auth.register

import android.util.Patterns
import com.example.mini_project.core.AuthService
import com.example.mini_project.ui.auth.base.BaseAuthViewModel

class RegisterViewModel : BaseAuthViewModel() {

    fun register(name: String, email: String, pass: String, confirm: String) {
        when {
            name.length < 2 -> showError("Enter your name")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> showError("Enter a valid email")
            pass.length < 6 -> showError("Password must be at least 6 characters")
            pass != confirm -> showError("Passwords do not match")
            else -> {
                _loading.value = true
                AuthService.register(name, email, pass, onResult)
            }
        }
    }
}