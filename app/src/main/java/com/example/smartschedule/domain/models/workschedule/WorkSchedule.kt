package com.example.smartschedule.domain.models.workschedule

import android.text.format.DateUtils
import com.example.smartschedule.domain.models.shift.Shift
import com.example.smartschedule.domain.models.shiftassignment.Assignment
import com.example.smartschedule.domain.models.shiftassignment.UserId
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
    fun getShiftAssignmentsForWeek() : List<Assignment> {
        val endWeek = startDate.plusDays(6)
        val shifts = assignments.filter { assignment ->
            val shiftDate = assignment.shift.startDate.toLocalDate()
            shiftDate in startDate..endWeek
        }
        return shifts
    }

    fun getShiftCountForWeek() : Int{
        return getShiftAssignmentsForWeek().size
    }

    fun getCoverShiftsPercentage(): Float {
        val totalShifts = getShiftCountForWeek()
        if (totalShifts == 0) return 0f
        val coveredShifts = assignments.filter {
            it.assignedEmployees.size == it.shift.strategy.getRequiredEmployees()
        }
        return (coveredShifts.size.toFloat() / totalShifts.toFloat()) * 100
    }


    fun getTodayAssignments(): List<Assignment> {
        return assignments.mapNotNull { assignment ->
            val shift = assignment.shift
            val shiftDate = LocalDate.of(
                shift.startDate.year,
                shift.startDate.month,
                shift.startDate.dayOfMonth
            )
            if (shiftDate.isEqual(LocalDate.now())) assignment else null
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

    fun getEmployeeAssignments(employeeId: String): List<Assignment> {
        return assignments.filter { assignment ->
            assignment.assignedEmployees.any { userId -> userId.trim() == employeeId.trim() }
        }
    }

    fun getNextAssignment(employeeId: String): Assignment? {
        val today = LocalDate.now()
        return getEmployeeAssignments(employeeId)
            .filter { it.shift.startDate.toLocalDate().isAfter(today) }
            .minByOrNull { it.shift.startDate }
    }
    fun getDates(): List<String> {
        val dateRange = mutableListOf<String>()
        var currentDate = startDate
        while (currentDate <= endDate) {
            dateRange.add(currentDate.dayOfMonth.toString())
            currentDate = currentDate.plusDays(1)
        }
        return dateRange
    }
    fun getWeekDays(): List<String> {
        val weekDays = mutableListOf<String>()
        var currentDate = startDate
        while (currentDate <= endDate) {
            weekDays.add(currentDate.dayOfWeek.toString())
            currentDate = currentDate.plusDays(1)
        }
        return weekDays
    }



}

enum class ScheduleStatus{
    DRAFT,
    PUBLISHED,
    ARCHIVED
}