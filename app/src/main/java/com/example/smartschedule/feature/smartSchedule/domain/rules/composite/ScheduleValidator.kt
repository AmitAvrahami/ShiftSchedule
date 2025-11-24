package com.example.smartschedule.feature.smartSchedule.domain.rules.composite

import com.example.smartschedule.core.domain.model.constraints.Constraint
import com.example.smartschedule.core.domain.model.constraints.WeeklyRule
import com.example.smartschedule.core.domain.model.smartSchedule.WorkSchedule
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.ValidationResult
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.ViolationSeverity
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.ScheduleRule

class ScheduleValidator(
    private val rules: List<ScheduleRule>
) {
    fun validate(
        schedule: WorkSchedule ,
        constraints: List<Constraint>,
        weeklyRules: List<WeeklyRule>
    ): ValidationResult {
        val all = rules.flatMap { it.validate(schedule, constraints, weeklyRules) }
        val errors = all.filter { it.severity == ViolationSeverity.ERROR }
        val warnings = all.filter { it.severity == ViolationSeverity.WARNING }
        return ValidationResult(
            errors ,
            warnings
        )
    }
}