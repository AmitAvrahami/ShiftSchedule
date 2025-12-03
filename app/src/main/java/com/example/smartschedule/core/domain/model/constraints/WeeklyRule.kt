package com.example.smartschedule.core.domain.model.constraints

import com.example.smartschedule.core.domain.model.constraints.enums.RuleKind
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.enums.ShiftType
import java.time.DayOfWeek

@JvmInline
value class WeeklyRuleId(val value: String)

/**
 * חוק שבועי קבוע לעובד (יום+סוג משמרת):
 * - יכול לייצג אילוץ קבוע (UNAVAILABLE)
 * - או העדפה
 * - או שיבוץ קבוע (MANDATORY)
 */
data class WeeklyRule(
    val id: WeeklyRuleId,
    val employeeId: EmployeeId,
    val dayOfWeek: DayOfWeek,
    val shiftType: ShiftType,
    val kind: RuleKind ,
    val isActive: Boolean,
    val notes: String?
)
