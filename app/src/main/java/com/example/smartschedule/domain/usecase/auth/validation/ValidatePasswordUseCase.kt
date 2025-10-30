package com.example.smartschedule.domain.usecase.auth.validation

import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {
    operator fun invoke(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(false, ValidationField.PASSWORD, "Password cannot be empty")
        }
        if (password.length < 6) {
            return ValidationResult(false, ValidationField.PASSWORD, "Password must be at least 6 characters long")
        }
        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }

        if (!hasUppercase || !hasLowercase || !hasDigit) {
            return ValidationResult(false, ValidationField.PASSWORD, "Password must contain upper, lower case letters and digits")
        }

        return ValidationResult(true)
    }
}
