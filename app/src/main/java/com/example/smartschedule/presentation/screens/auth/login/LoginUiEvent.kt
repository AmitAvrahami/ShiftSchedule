package com.example.smartschedule.presentation.screens.auth.login

sealed class LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent()
    data class PasswordChanged(val password: String) : LoginUiEvent()
    object OnLoginClicked : LoginUiEvent()
    object OnSignUpClicked : LoginUiEvent()
    object OnForgotPasswordClicked : LoginUiEvent()
    object TogglePasswordVisibility : LoginUiEvent()
}
