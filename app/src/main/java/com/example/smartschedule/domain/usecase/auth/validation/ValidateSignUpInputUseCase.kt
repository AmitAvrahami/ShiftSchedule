package com.example.smartschedule.domain.usecase.auth.validation

import javax.inject.Inject

class ValidateSignUpInputUseCase @Inject constructor(
    private val validateFullName: ValidateFullNameUseCase,
    private val validateNationalId: ValidateNationalIdUseCase,
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase
) {
    operator fun invoke(
        fullName: String,
        nationalId: String,
        email: String?,
        password: String,
        confirmPassword: String,
        agreeToTerms: Boolean
    ): ValidationResult {

        val fullNameResult = validateFullName(fullName)
        if (!fullNameResult.isSuccess) return fullNameResult

        val nationalIdResult = validateNationalId(nationalId)
        if (!nationalIdResult.isSuccess) return nationalIdResult

        if (!email.isNullOrBlank()) {
            val emailResult = validateEmail(email)
            if (!emailResult.isSuccess) return emailResult
        }

        val passwordResult = validatePassword(password)
        if (!passwordResult.isSuccess) return passwordResult

        if (password != confirmPassword) {
            return ValidationResult(
                isSuccess = false,
                field = ValidationField.CONFIRM_PASSWORD,
                errorMessage = "Passwords do not match"
            )
        }

        if (!agreeToTerms) {
            return ValidationResult(
                isSuccess = false,
                field = ValidationField.TERMS,
                errorMessage = "You must agree to the terms and policy"
            )
        }

        return ValidationResult(true)
    }
}
