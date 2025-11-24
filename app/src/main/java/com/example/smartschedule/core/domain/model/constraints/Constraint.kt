package com.example.smartschedule.core.domain.model.constraints

import com.example.smartschedule.core.domain.model.constraints.enums.ConstraintType
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import java.time.LocalDate
import java.time.LocalTime

@JvmInline
value class ConstraintId(val value: Int)

/**
 * אילוץ חד-פעמי או על טווח תאריכים.
 * משמש כחוסר זמינות "מוחשי" לתאריכים ספציפיים.
 */
data class Constraint(
    val id: ConstraintId,
    val employeeId: EmployeeId,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val startTime: LocalTime?,  // null = כל היום
    val endTime: LocalTime?,    // null = כל היום
    val type: ConstraintType ,
    val reason: String?
)
