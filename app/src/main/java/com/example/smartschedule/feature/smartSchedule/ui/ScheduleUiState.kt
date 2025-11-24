package com.example.smartschedule.feature.smartSchedule.ui

import com.example.smartschedule.core.domain.model.employees.Employee
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.Shift
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftAssignment
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftId
import com.example.smartschedule.core.domain.model.smartSchedule.WorkSchedule
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.RuleViolation
import java.time.LocalDate

sealed interface ScheduleUiState {
    // 1. מצב טעינה ראשוני
    data object Loading : ScheduleUiState

    // 2. מצב הצלחה - מכיל את כל הנתונים + תוצאות הולידציה
    data class Success(
        val schedule: WorkSchedule,
        val employees: List<Employee>,
        val selectedShift: Shift? = null,
        val violations: List<RuleViolation> = emptyList(),
        val shiftsByDate: Map<LocalDate , List<Shift>> = emptyMap(),
        val assignmentsByShiftId: Map<ShiftId, List<ShiftAssignment>> = emptyMap(),
        val employeesById: Map<EmployeeId , Employee> = emptyMap(),
        val violationsByShiftId: Map<ShiftId?, List<RuleViolation>> = emptyMap(),
        val daysInRange: List<LocalDate> = emptyList()
    ) : ScheduleUiState

    // 3. מצב שגיאה
    data class Error(val message: String) : ScheduleUiState
}