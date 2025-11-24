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
import java.time.temporal.IsoFields

class MaxNightsPerWeekRule(
    private val maxNights: Int = 2,
    private val nightShiftTypes: Set<ShiftType> = setOf(ShiftType.NIGHT)
) : ScheduleRule {

    override val id : String = "max_nights_per_week"
    override val name : String = "הגבלת כמות לילות שבועית (עד $maxNights)"

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

            val employeeNightShifts = assignments.mapNotNull { shiftsById[it.shiftId] }
                .filter { it.shiftType in nightShiftTypes }

            val shiftsByWeek = employeeNightShifts.groupBy { shift ->
                shift.date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
            }

            shiftsByWeek.forEach { (weekNumber , shiftsInWeek) ->
                if (shiftsInWeek.size > maxNights) {
                    val lastShiftInWeek = shiftsInWeek.maxByOrNull { it.date }

                    if (lastShiftInWeek != null) {
                        violations.add(
                            buildViolation(
                                employeeId ,
                                lastShiftInWeek ,
                                shiftsInWeek.size ,
                                weekNumber
                            )
                        )
                    }
                }
            }
        }

        return violations
    }

    private fun buildViolation(
        employeeId : EmployeeId ,
        shift : Shift ,
        count : Int ,
        weekNumber : Int
    ) : RuleViolation {
        return RuleViolation(
            ruleId = id ,
            severity = ViolationSeverity.WARNING ,
            message = "עומס לילות: לעובד $employeeId יש $count משמרות לילה בשבוע מס' $weekNumber (המקסימום המומלץ הוא $maxNights)." ,
            relatedShiftId = shift.id ,
            relatedEmployeeId = employeeId
        )
    }
}