package com.example.smartschedule.domain.usecase.auth.validation

import javax.inject.Inject

class ValidateFullNameUseCase @Inject constructor() {
    operator fun invoke(fullName: String): ValidationResult {
        if (fullName.isBlank()) {
            return ValidationResult(
                isSuccess = false,
                field = ValidationField.FULL_NAME,
                errorMessage = "Full name cannot be empty"
            )
        }
        if (fullName.length < 3) {
            return ValidationResult(
                isSuccess = false,
                field = ValidationField.FULL_NAME,
                errorMessage = "Full name is too short"
            )
        }
        return ValidationResult(isSuccess = true)
    }
}
