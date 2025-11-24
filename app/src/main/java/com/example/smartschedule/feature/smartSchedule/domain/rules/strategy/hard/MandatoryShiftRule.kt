package com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.hard

import com.example.smartschedule.core.domain.model.constraints.Constraint
import com.example.smartschedule.core.domain.model.constraints.WeeklyRule
import com.example.smartschedule.core.domain.model.constraints.enums.ConstraintType
import com.example.smartschedule.core.domain.model.constraints.enums.RuleKind
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.Shift
import com.example.smartschedule.core.domain.model.smartSchedule.WorkSchedule
import com.example.smartschedule.core.domain.model.smartSchedule.enums.AssignmentStatus
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.RuleViolation
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.ViolationSeverity
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.ScheduleRule
import java.time.LocalDate
import java.time.LocalTime

class MandatoryShiftRule : ScheduleRule {

    override val id: String = "mandatory_fixed_shift"
    override val name: String = "עובדים קבועים (חובה לשבץ אלא אם יש אילוץ)"

    override fun validate(
        schedule: WorkSchedule,
        constraints: List<Constraint>,
        weeklyRules: List<WeeklyRule>
    ): List<RuleViolation> {

        val violations = mutableListOf<RuleViolation>()


        val mandatoryRules = weeklyRules.filter {
            it.kind == RuleKind.MANDATORY && it.isActive
        }

        val constraintsByEmployee = constraints.groupBy { it.employeeId }

        val assignmentsMap = schedule.assignments
            .filter { it.status == AssignmentStatus.ACTIVE }
            .groupBy { it.shiftId }
            .mapValues { entry -> entry.value.map { it.employeeId }.toSet() }

        for (shift in schedule.shifts) {
            val requiredEmployeesRules = mandatoryRules.filter { rule ->
                rule.dayOfWeek == shift.date.dayOfWeek &&
                        rule.shiftType == shift.shiftType
            }

            for (rule in requiredEmployeesRules) {
                val fixedEmployeeId = EmployeeId(rule.employeeId)

                val isEmployeeUnavailable = isEmployeeBlockedByConstraint(
                    shift,
                    constraintsByEmployee[fixedEmployeeId] ?: emptyList()
                )

                if (isEmployeeUnavailable) continue

                val currentAssignees = assignmentsMap[shift.id] ?: emptySet()

                if (fixedEmployeeId !in currentAssignees) {
                    violations.add(
                        RuleViolation(
                            ruleId = id,
                            severity = ViolationSeverity.ERROR,
                            message = "העובד $fixedEmployeeId הוא עובד קבוע במשמרות ${shift.shiftType.displayName} ביום ${translateDay(shift.date.dayOfWeek)}, אך לא שובץ (ולא הגיש אילוץ).",
                            relatedShiftId = shift.id,
                            relatedEmployeeId = fixedEmployeeId
                        )
                    )
                }
            }
        }

        return violations
    }

    /**
     * בודק האם לעובד יש אילוץ שמתנגש עם המשמרת הספציפית הזו
     */
    private fun isEmployeeBlockedByConstraint(
        shift: Shift,
        employeeConstraints: List<Constraint>
    ): Boolean {
        return employeeConstraints.any { constraint ->
            // בדיקה ראשונית: האם התאריך בטווח?
            val isDateMatch = !shift.date.isBefore(constraint.startDate) &&
                    !shift.date.isAfter(constraint.endDate)

            if (!isDateMatch) return@any false

            when (constraint.type) {
                ConstraintType.FULL_DAY, ConstraintType.DATE_RANGE -> true
                ConstraintType.PARTIAL_DAY -> {

                    val conStart = constraint.startTime ?: LocalTime.MIN
                    val conEnd = constraint.endTime ?: LocalTime.MAX

                    // שימוש בלוגיקה של חפיפת זמנים (אפשר להעתיק ממחלקות קודמות או להשתמש ב-Util)
                    timeRangesOverlap(shift.startTime, shift.endTime, conStart, conEnd)
                }
                else -> false
            }
        }
    }

    private fun timeRangesOverlap(s1: LocalTime, e1: LocalTime, s2: LocalTime, e2: LocalTime): Boolean {
        return !e1.isBefore(s2) && !s1.isAfter(e2)
    }

    private fun translateDay(day: java.time.DayOfWeek): String {
        // אפשר להחליף ב-Resource String
        return when(day) {
            java.time.DayOfWeek.SUNDAY -> "ראשון"
            java.time.DayOfWeek.MONDAY -> "שני"
            // ...
            else -> day.name
        }
    }
}