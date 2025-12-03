package com.example.smartschedule.core.domain.model.constraints

import com.example.smartschedule.core.domain.model.constraints.enums.ConstraintType
import com.example.smartschedule.core.domain.model.constraints.enums.RuleKind
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.enums.ShiftType
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * פונקציות עזר להפקת Constraints בפועל מחוקים שבועיים.
 */
object ConstraintMaterializer {

    /**
     * הפקת אילוצים מתוך רשימת WeeklyRule עבור טווח תאריכים מסוים.
     * לדוגמה: יצירת אילוצים קבועים לכל יום שני בוקר בחודש.
     */
    fun materializeWeeklyRulesAsConstraints(
        rules: List<WeeklyRule>,
        dateRange: ClosedRange<LocalDate>,
        idGenerator: () -> ConstraintId
    ): List<Constraint> {
        val constraints = mutableListOf<Constraint>()

        var current = dateRange.start
        while (!current.isAfter(dateRange.endInclusive)) {
            val dayOfWeek = current.dayOfWeek

            rules.filter { it.isActive && it.dayOfWeek == dayOfWeek }
                .forEach { rule ->
                    if (rule.kind == RuleKind.UNAVAILABLE) {
                        constraints += rule.toConstraintForDate(
                            date = current,
                            id = idGenerator()
                        )
                    }
                    // kind = PREFERRED / MANDATORY יכולים להיות מטופלים
                    // בחוקי ולידציה/שיבוץ, לא בהכרח כאילוץ "חסום"
                }

            current = current.plus(1, ChronoUnit.DAYS)
        }

        return constraints
    }

    /**
     * יצירת Constraint בודד מתאריך נתון ו-WeeklyRule מסוג UNAVAILABLE.
     */
    private fun WeeklyRule.toConstraintForDate(
        date: LocalDate,
        id: ConstraintId
    ): Constraint {
        val times = shiftType.toTimeRange()

        return Constraint(
            id = id,
            employeeId = EmployeeId(employeeId.value),
            startDate = date,
            endDate = date,
            startTime = times.first,
            endTime = times.second,
            type = ConstraintType.FULL_DAY, // או PARTIAL_DAY לפי הלוגיקה
            reason = notes
        )
    }

    /**
     * המרה מ-ShiftType לזוג שעות (start,end) – אם צריך.
     * אפשר לקרוא לזה גם מתוך מקום אחר בדומיין.
     */
    private fun ShiftType.toTimeRange() = defaultStartTime to defaultEndTime
}
