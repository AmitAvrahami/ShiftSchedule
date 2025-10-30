package com.example.smartschedule.domain.usecase.auth.validation

import android.util.Patterns

class ValidateEmailUseCase {
    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                isSuccess = false,
                field = ValidationField.EMAIL,
                errorMessage = "The email can\'t be blank."
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                isSuccess = false,
                field = ValidationField.EMAIL,
                errorMessage = "That\'s not a valid email."
            )
        }
        return ValidationResult(
            isSuccess = true
        )
    }
}
