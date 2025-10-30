package com.example.smartschedule.presentation.screens.auth.signup

import com.example.smartschedule.domain.usecase.auth.validation.ValidationField

data class SignUpState(
    val fullName: String = "",
    val nationalId: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val agreeToTerms: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorField: ValidationField? = null,
    val error: String? = null
)