package com.example.smartschedule.presentation.screens.auth.signup

import com.example.smartschedule.domain.models.user.roles.Roles

sealed class SignUpUiEvent {
    data class FullNameChanged(val fullName: String) : SignUpUiEvent()
    data class NationalIdChanged(val nationalId: String) : SignUpUiEvent()
    data class EmailChanged(val email: String) : SignUpUiEvent()
    data class PasswordChanged(val password: String) : SignUpUiEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpUiEvent()
    data class ToggleRole(val role: Roles) : SignUpUiEvent()
    data class AgreeToTermsChanged(val agreeToTerms: Boolean) : SignUpUiEvent()
    object TogglePasswordVisibility : SignUpUiEvent()
    object ToggleConfirmPasswordVisibility : SignUpUiEvent()
    object SignUpClicked : SignUpUiEvent()
    object LoginClicked : SignUpUiEvent()
}