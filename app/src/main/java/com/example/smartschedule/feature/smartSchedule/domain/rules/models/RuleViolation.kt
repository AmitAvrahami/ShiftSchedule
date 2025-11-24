package com.example.smartschedule.feature.smartSchedule.domain.rules.models

import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftId
import java.util.UUID

data class RuleViolation(
    val id: String = UUID.randomUUID().toString(),
    val ruleId: String,
    val severity: ViolationSeverity,
    val message: String,
    val relatedShiftId: ShiftId? = null,
    val relatedEmployeeId: EmployeeId? = null
)