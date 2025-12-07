package com.example.smartschedule.core.data.firebase.model

import java.time.DayOfWeek

data class WeeklyRuleDto(
    val ruleId: String = "",
    val employeeId: String = "",
    val dayOfWeek: String = DayOfWeek.MONDAY.name,
    val shiftType: String = "MORNING",
    val kind: String = "UNAVAILABLE",
    val isActive: Boolean = true,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)