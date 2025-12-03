package com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.hard

import com.example.smartschedule.core.domain.model.constraints.Constraint
import com.example.smartschedule.core.domain.model.constraints.WeeklyRule
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.Shift
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftId
import com.example.smartschedule.core.domain.model.smartSchedule.WorkSchedule
import com.example.smartschedule.core.domain.model.smartSchedule.enums.AssignmentStatus
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.RuleViolation
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.ViolationSeverity
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.ScheduleRule
import java.time.LocalDateTime

class DoubleBookingRule : ScheduleRule {
    override val id: String = "no_overlapping_shifts"
    override val name: String = "מניעת כפילות משמרות (חפיפת זמנים)"

    override fun validate(
        schedule: WorkSchedule ,
        constraints: List<Constraint>, // לא רלוונטי לחוק הזה
        weeklyRules: List<WeeklyRule>  // לא רלוונטי לחוק הזה
    ): List<RuleViolation> {

        val violations = mutableListOf<RuleViolation>()

        val shiftsById = schedule.shifts.associateBy { it.id }

        val assignmentsByEmployee = schedule.assignments
            .filter { it.status == AssignmentStatus.ACTIVE }
            .groupBy { it.employeeId }

        assignmentsByEmployee.forEach { (employeeId, assignments) ->

            val employeeRanges = assignments.mapNotNull { assignment ->
                val shift = shiftsById[assignment.shiftId] ?: return@mapNotNull null
                convertToTimeRange(assignment.id.value, employeeId, shift)
            }.sortedBy { it.start }

            for (i in 0 until employeeRanges.size - 1) {
                val currentShift = employeeRanges[i]
                val nextShift = employeeRanges[i + 1]

                if (nextShift.start.isBefore(currentShift.end)) {
                    violations.add(
                        RuleViolation(
                            ruleId = id,
                            severity = ViolationSeverity.ERROR,
                            message = "כפילות משמרות: עובד $employeeId שובץ למשמרת ${nextShift.shiftName} שחופפת למשמרת ${currentShift.shiftName}",
                            relatedShiftId = nextShift.shiftId, // מסמנים את המשמרת השנייה כבעייתית
                            relatedEmployeeId = employeeId
                        )
                    )
                }
            }
        }

        return violations
    }

    private fun convertToTimeRange(
        assignmentId: String,
        employeeId: EmployeeId,
        shift: Shift
    ): ShiftTimeRange {
        val startDateTime = LocalDateTime.of(shift.date, shift.startTime)

        var endDateTime = LocalDateTime.of(shift.date, shift.endTime)

        // טיפול קריטי: אם שעת הסיום קטנה משעת ההתחלה, סימן שעברנו לחצות (יום המחרת)
        // לדוגמה: התחלה 22:00, סיום 06:00
        if (endDateTime.isBefore(startDateTime) || endDateTime.isEqual(startDateTime)) {
            // (isEqual זה למקרה קצה של משמרת 24 שעות, נדיר אבל קיים)
            endDateTime = endDateTime.plusDays(1)
        }

        return ShiftTimeRange(
            assignmentId = assignmentId,
            shiftId = shift.id,
            employeeId = employeeId,
            start = startDateTime,
            end = endDateTime,
            shiftName = "${shift.date} (${shift.shiftType.displayName})" // למשל: "2023-10-01 (בוקר)"
        )
    }

    // מבנה עזר פנימי
    private data class ShiftTimeRange(
        val assignmentId: String,
        val shiftId: ShiftId ,
        val employeeId: EmployeeId ,
        val start: LocalDateTime,
        val end: LocalDateTime ,
        val shiftName: String
    )
}