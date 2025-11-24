package com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.soft

import com.example.smartschedule.core.domain.model.constraints.Constraint
import com.example.smartschedule.core.domain.model.constraints.WeeklyRule
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.WorkSchedule
import com.example.smartschedule.core.domain.model.smartSchedule.enums.AssignmentStatus
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.RuleViolation
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.ViolationSeverity
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.ScheduleRule
import kotlin.math.roundToInt

/**
 * חוק איזון עומסים: בודק חלוקה הוגנת של כמות המשמרות בין העובדים.
 * @param allowedDeviation הסטייה המותרת מהממוצע (למשל 2 משמרות פלוס/מינוס).
 */
class FairWorkloadRule(
    private val allowedDeviation: Int = 2
) : ScheduleRule {

    override val id: String = "fair_workload_balance"
    override val name: String = "איזון עומסים (חלוקה הוגנת)"

    override fun validate(
        schedule: WorkSchedule,
        constraints: List<Constraint>,
        weeklyRules: List<WeeklyRule>
    ): List<RuleViolation> {

        val violations = mutableListOf<RuleViolation>()

        val shiftsPerEmployee = schedule.assignments
            .filter { it.status == AssignmentStatus.ACTIVE }
            .groupingBy { it.employeeId }
            .eachCount()

        if (shiftsPerEmployee.isEmpty()) return emptyList()

        val totalShifts = shiftsPerEmployee.values.sum()
        val activeEmployeesCount = shiftsPerEmployee.size
        val averageShifts = totalShifts.toDouble() / activeEmployeesCount

        shiftsPerEmployee.forEach { (employeeId, count) ->
            val deviation = count - averageShifts

            if (deviation > allowedDeviation) {
                violations.add(
                    buildViolation(
                        employeeId,
                        count,
                        averageShifts,
                        isOverworked = true
                    )
                )
            }

            if (deviation < -allowedDeviation) {
                violations.add(
                    buildViolation(
                        employeeId,
                        count,
                        averageShifts,
                        isOverworked = false
                    )
                )
            }
        }

        return violations
    }

    private fun buildViolation(
        employeeId: EmployeeId,
        count: Int,
        average: Double,
        isOverworked: Boolean
    ): RuleViolation {
        val typeString = if (isOverworked) "עומס יתר" else "חוסר משמרות"
        val avgString = "%.1f".format(average)

        return RuleViolation(
            ruleId = id,
            severity = ViolationSeverity.WARNING, // אזהרה בלבד
            message = "חוסר איזון ($typeString): העובד $employeeId קיבל $count משמרות, בעוד הממוצע הוא $avgString (חריגה מהטווח המותר).",
            relatedEmployeeId = employeeId,
            relatedShiftId = null
        )
    }
}