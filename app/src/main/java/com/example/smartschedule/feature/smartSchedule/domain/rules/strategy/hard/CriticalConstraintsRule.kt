package com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.hard

import com.example.smartschedule.core.domain.model.constraints.Constraint
import com.example.smartschedule.core.domain.model.constraints.WeeklyRule
import com.example.smartschedule.core.domain.model.constraints.enums.ConstraintType
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.WorkSchedule
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.RuleViolation
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.ViolationSeverity
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.ScheduleRule
import java.time.LocalTime

class CriticalConstraintsRule : ScheduleRule {

    override val id : String = "critical_constraints"
    override val name : String = "אילוצים קריטיים – אי שיבוץ עובד בניגוד לאילוץ"


    override fun validate(
        schedule : WorkSchedule ,
        constraints : List<Constraint> ,
        weeklyRules : List<WeeklyRule>
    ) : List<RuleViolation> {

        val shiftsById = schedule.shifts.associateBy { it.id.value }
        val violations = mutableListOf<RuleViolation>()

        schedule.assignments.forEach { assignment ->
            val shift = shiftsById[assignment.shiftId.value] ?: return@forEach

            val employeeId = assignment.employeeId

            val employeeConstraints = constraints.filter { it.employeeId == employeeId }

            employeeConstraints.forEach { constraint ->
                val dateInRange =
                    ! shift.date.isBefore(constraint.startDate) &&
                            ! shift.date.isAfter(constraint.endDate)

                if (! dateInRange) return@forEach

                when (constraint.type){
                    ConstraintType.FULL_DAY ,
                    ConstraintType.DATE_RANGE -> {
                        violations += buildViolationForConstraint(employeeId, shift)
                    }
                    ConstraintType.PARTIAL_DAY -> {
                        val cStart = constraint.startTime ?: LocalTime.MIN
                        val cEnd = constraint.endTime ?: LocalTime.MAX
                        if (timeRangesOverlap(shift.startTime, shift.endTime, cStart, cEnd)
                        ) {
                            violations += buildViolationForConstraint(employeeId, shift)
                        }
                    }
                    ConstraintType.WEEKLY_RECURRING ->{}
                }
            }
        }
        return violations

    }
    private fun buildViolationForConstraint(
        employeeId: EmployeeId,
        shift: com.example.smartschedule.core.domain.model.smartSchedule.Shift
    ): RuleViolation {
        return RuleViolation(
            ruleId = id,
            message = "העובד $employeeId שובץ במשמרת בניגוד לאילוץ בתאריך ${shift.date} (${shift.shiftType.displayName})",
            relatedShiftId = shift.id,
            relatedEmployeeId = employeeId,
            severity = ViolationSeverity.ERROR
        )
    }

    private fun timeRangesOverlap(
        s1: LocalTime,
        e1: LocalTime,
        s2: LocalTime,
        e2: LocalTime
    ): Boolean {
        return !e1.isBefore(s2) && !s1.isAfter(e2)
    }

}