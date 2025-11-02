package com.example.smartschedule.domain.models.workschedule

import android.text.format.DateUtils
import com.example.smartschedule.domain.models.shift.Shift
import com.example.smartschedule.domain.models.shiftassignment.Assignment
import com.example.smartschedule.domain.models.user.roles.EmployeeRole
import com.example.smartschedule.domain.models.user.roles.ManagerRole
import java.time.LocalDate
import java.time.LocalDateTime

data class WorkSchedule(
    val id: Long,
    val title: String,
    val startDate : LocalDate,
    val endDate : LocalDate,
    val status : ScheduleStatus,
    val assignments : List<Assignment>,
    val createdAt : LocalDateTime,
    val updatedAt : LocalDateTime,
    val createdBy : EmployeeRole,
    val approveBy : ManagerRole,
    val notes : String? = null
) {
    fun getShiftCountForWeek(): Int {
        val endWeek = startDate.plusDays(6)
        val shifts = assignments.filter { assignment ->
            val shift = assignment.shift
            val shiftDate = LocalDate.of(
                shift.startDate.year,
                shift.startDate.month,
                shift.startDate.dayOfMonth
            )
            shiftDate.isAfter(startDate) && shiftDate.isBefore(endWeek)
        }
        return shifts.size
    }

    fun getCoverShiftsPercentage(): Float {
        val totalShifts = getShiftCountForWeek()
        val coveredShifts = assignments.filter { assignment ->
            assignment.assignedEmployees.size == assignment.shift.strategy.getRequiredEmployees()
        }
        return (coveredShifts.size.toFloat() / totalShifts.toFloat()) * 100
    }

    fun getTodayShifts(): List<Shift> {
        return assignments.mapNotNull { assignment ->
            val shift = assignment.shift
            val shiftDate = LocalDate.of(
                shift.startDate.year,
                shift.startDate.month,
                shift.startDate.dayOfMonth
            )
            if (shiftDate.isEqual(LocalDate.now())) assignment.shift else null
        }
    }

    fun getCoverShifts(): List<Assignment> {
        return assignments.filter { assignment ->
            assignment.assignedEmployees.size == assignment.shift.strategy.getRequiredEmployees()
        }
    }

    fun isWeeklyPeriod(): Boolean {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) == 6L
    }

    fun getShiftsByDate(date: LocalDate): List<Shift> {
        return assignments
            .map { it.shift }
            .filter { it.startDate.toLocalDate().isEqual(date) }
    }

}

enum class ScheduleStatus{
    DRAFT,
    PUBLISHED,
    ARCHIVED
}