package com.example.smartschedule.domain.models.shift

import com.example.smartschedule.domain.models.constraint.ConstraintStrategy
import com.example.smartschedule.domain.models.employee.Employee
import java.time.LocalDateTime

data class Shift(
    val id: Long,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val strategy: ShiftStrategy,
    val assignedEmployees: List<Employee>
)

interface ShiftStrategy{
    fun getStartTime(): LocalDateTime
    fun getEndTime(): LocalDateTime
    fun getRequiredEmployees(): Int
    fun isOverNight(): Boolean

}
