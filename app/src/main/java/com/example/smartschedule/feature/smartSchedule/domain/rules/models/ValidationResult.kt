package com.example.smartschedule.feature.smartSchedule.domain.rules.models

data class ValidationResult(
    val errors: List<RuleViolation>,
    val warnings: List<RuleViolation>
) {
    val isValid: Boolean get() = errors.isEmpty()
}