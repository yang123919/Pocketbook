package com.example.mini_project.ui.home

import androidx.lifecycle.ViewModel
import com.example.mini_project.core.AuthService

class HomeViewModel : ViewModel() {

    val userName: String get() = AuthService.displayName

    fun logout() = AuthService.logout()
}