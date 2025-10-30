package com.example.smartschedule.presentation.screens.auth.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val error: String? = null
)
