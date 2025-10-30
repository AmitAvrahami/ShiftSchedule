package com.example.smartschedule.domain.usecase.auth.validation

data class ValidationResult(
    val isSuccess: Boolean,
    val field: ValidationField? = null,
    val errorMessage: String? = null

)

enum class ValidationField {
    FULL_NAME, NATIONAL_ID, EMAIL, PASSWORD, CONFIRM_PASSWORD, TERMS
}