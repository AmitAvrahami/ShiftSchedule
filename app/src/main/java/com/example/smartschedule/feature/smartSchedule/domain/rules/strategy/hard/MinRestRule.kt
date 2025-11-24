package com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.hard

import com.example.smartschedule.core.domain.model.constraints.Constraint
import com.example.smartschedule.core.domain.model.constraints.WeeklyRule
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.WorkSchedule
import com.example.smartschedule.core.domain.model.smartSchedule.enums.AssignmentStatus
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.RuleViolation
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.ViolationSeverity
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.ScheduleRule
import java.time.Duration
import java.time.LocalDateTime

class MinRestRule(
    private val minRestHoursHard: Long = 8L,
    private val recommendedRestHours: Long = 16L
): ScheduleRule {

    override val id : String = "min_rest_between_shifts"

    override val name : String =
        "מנוחה מינימלית בין משמרות (פחות מ-$minRestHoursHard שעות = קריטי, פחות מ-$recommendedRestHours שעות = אזהרה)"

    override fun validate(
        schedule : WorkSchedule ,
        constraints : List<Constraint> ,
        weeklyRules : List<WeeklyRule>
    ) : List<RuleViolation> {

        val violations = mutableListOf<RuleViolation>()

        val shiftsById = schedule.shifts.associateBy { it.id }

        val assignmentByEmployee = schedule.assignments
            .filter { it.status == AssignmentStatus.ACTIVE }
            .groupBy { it.employeeId }

        assignmentByEmployee.forEach { (employeeId , employeeAssignments) ->
            val employeeShifts = employeeAssignments.mapNotNull { assignment ->
                val shift = shiftsById[assignment.shiftId] ?: return@mapNotNull null

                val start = LocalDateTime.of(
                    shift.date ,
                    shift.startTime
                )
                var end = LocalDateTime.of(
                    shift.date ,
                    shift.endTime
                )

                if (end.isBefore(start)) {
                    end = end.plusDays(1)
                }

                EmployeeShiftWithTimes(
                    employeeId = employeeId ,
                    shiftId = shift.id ,
                    shiftDate = shift.date ,
                    shiftTypeDisplayName = shift.shiftType.displayName ,
                    start = start ,
                    end = end
                )
            }.sortedBy { it.start }

            if (employeeShifts.size < 2) return@forEach

            for (i in 0 until employeeShifts.size - 1) {
                val prev = employeeShifts[i]
                val next = employeeShifts[i + 1]

                val restDuration = Duration.between(
                    prev.end ,
                    next.start
                )
                val restHours = restDuration.toHours()

                if (restDuration.isNegative || restDuration.isZero) continue

                when {
                    restHours <= minRestHoursHard -> {
                        violations += RuleViolation(
                            ruleId = id ,
                            message = buildHardViolationMessage(
                                employeeId ,
                                prev ,
                                next ,
                                restHours
                            ) ,
                            relatedShiftId = next.shiftId ,
                            relatedEmployeeId = employeeId ,
                            severity = ViolationSeverity.ERROR
                        )
                    }

                    restHours < recommendedRestHours -> {
                        violations += RuleViolation(
                            ruleId = id ,
                            message = buildSoftViolationMessage(
                                employeeId ,
                                prev ,
                                next ,
                                restHours
                            ) ,
                            relatedShiftId = next.shiftId ,
                            relatedEmployeeId = employeeId ,
                            severity = ViolationSeverity.WARNING
                        )
                    }
                }
            }
        }

        return violations
    }


    private fun buildHardViolationMessage(
        employeeId : EmployeeId ,
        prev : EmployeeShiftWithTimes ,
        next : EmployeeShiftWithTimes ,
        restHours : Long
    ) : String {
        return "העובד $employeeId נח רק $restHours שעות בין משמרת " +
                "${prev.shiftTypeDisplayName} בתאריך ${prev.shiftDate} " +
                "למשמרת ${next.shiftTypeDisplayName} בתאריך ${next.shiftDate} – פחות מ-$minRestHoursHard שעות מנוחה!"
    }

    private fun buildSoftViolationMessage(
        employeeId : EmployeeId ,
        prev : EmployeeShiftWithTimes ,
        next : EmployeeShiftWithTimes ,
        restHours : Long
    ) : String {
        return "העובד $employeeId נח $restHours שעות בין משמרת " +
                "${prev.shiftTypeDisplayName} בתאריך ${prev.shiftDate} " +
                "למשמרת ${next.shiftTypeDisplayName} בתאריך ${next.shiftDate} – מומלצת מנוחה של $recommendedRestHours שעות."
    }

    private data class EmployeeShiftWithTimes(
        val employeeId : EmployeeId ,
        val shiftId : com.example.smartschedule.core.domain.model.smartSchedule.ShiftId ,
        val shiftDate : java.time.LocalDate ,
        val shiftTypeDisplayName : String ,
        val start : LocalDateTime ,
        val end : LocalDateTime
    )

}