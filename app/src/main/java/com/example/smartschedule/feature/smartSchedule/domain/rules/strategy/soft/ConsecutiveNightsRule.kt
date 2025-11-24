package com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.soft

import com.example.smartschedule.core.domain.model.constraints.Constraint
import com.example.smartschedule.core.domain.model.constraints.WeeklyRule
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.Shift
import com.example.smartschedule.core.domain.model.smartSchedule.WorkSchedule
import com.example.smartschedule.core.domain.model.smartSchedule.enums.AssignmentStatus
import com.example.smartschedule.core.domain.model.smartSchedule.enums.ShiftType
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.RuleViolation
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.ViolationSeverity
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.ScheduleRule

class ConsecutiveNightsRule(
    private val maxConsecutive: Int = 2,
    private val nightShiftTypes: Set<ShiftType> = setOf(ShiftType.NIGHT)
) : ScheduleRule {

    override val id : String = "max_consecutive_nights"
    override val name : String = "הגבלת רצף לילות (מקסימום $maxConsecutive)"

    override fun validate(
        schedule : WorkSchedule ,
        constraints : List<Constraint> ,
        weeklyRules : List<WeeklyRule>
    ) : List<RuleViolation> {

        val violations = mutableListOf<RuleViolation>()
        val shiftsById = schedule.shifts.associateBy { it.id }

        val assignmentsByEmployee = schedule.assignments
            .filter { it.status == AssignmentStatus.ACTIVE }
            .groupBy { it.employeeId }

        assignmentsByEmployee.forEach { (employeeId , assignments) ->

            val assignmentsByEmployee = schedule.assignments
                .filter { it.status == AssignmentStatus.ACTIVE }
                .groupBy { it.employeeId }

            val nightShifts = assignments.mapNotNull { shiftsById[it.shiftId] }
                .filter { it.shiftType in nightShiftTypes } // בודקים אם זה סוג "לילה"
                .sortedBy { it.date }

            if (nightShifts.size <= maxConsecutive) return@forEach

            var currentSequence = 1
            for (i in 1 until nightShifts.size) {
                val prevShift = nightShifts[i - 1]
                val currentShift = nightShifts[i]

                if (currentShift.date == prevShift.date.plusDays(1)) {
                    currentSequence ++
                } else {
                    currentSequence = 1
                }

                if (currentSequence > maxConsecutive) {
                    violations.add(
                        buildViolation(
                            employeeId ,
                            currentShift ,
                            currentSequence
                        )
                    )
                }
            }

        }
        return violations
    }


    private fun buildViolation(
        employeeId : EmployeeId ,
        shift : Shift ,
        sequenceSize : Int
    ) : RuleViolation {
        return RuleViolation(
            ruleId = id ,
            severity = ViolationSeverity.WARNING , // זה רק אזהרה, לא חוסם
            message = "רצף לילות חריג: העובד $employeeId משובץ ל-$sequenceSize משמרות לילה רצופות (האחרונה ב-${shift.date}). מומלץ לא יותר מ-$maxConsecutive." ,
            relatedShiftId = shift.id ,
            relatedEmployeeId = employeeId
        )
    }
}