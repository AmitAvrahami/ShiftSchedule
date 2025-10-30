package com.example.smartschedule.domain.usecase.auth.validation

import javax.inject.Inject

class ValidateNationalIdUseCase @Inject constructor() {

    operator fun invoke(nationalId: String): ValidationResult {
        if (nationalId.isBlank()) {
            return ValidationResult(false, ValidationField.NATIONAL_ID, "National ID cannot be empty")
        }
        if (!nationalId.all { it.isDigit() }) {
            return ValidationResult(false, ValidationField.NATIONAL_ID, "National ID must contain digits only")
        }

        val id = nationalId.padStart(9, '0')
        if (!isValidIsraeliId(id)) {
            return ValidationResult(false, ValidationField.NATIONAL_ID, "Invalid National ID")
        }

        return ValidationResult(true)
    }

    private fun isValidIsraeliId(id: String): Boolean {
        if (id.length != 9) return false
        val sum = id.mapIndexed { index, c ->
            var num = Character.getNumericValue(c)
            num *= if (index % 2 == 0) 1 else 2
            if (num > 9) num -= 9
            num
        }.sum()
        return sum % 10 == 0
    }
}
