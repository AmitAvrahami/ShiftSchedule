package com.example.smartschedule.feature.employees.ui

import com.example.smartschedule.core.domain.model.employees.Employee
import com.example.smartschedule.core.domain.model.smartSchedule.Shift
import java.time.LocalDate

data class EmployeeUiState(
    val currentEmployee: Employee? = null,
    val nextShift: Shift? = null,
    val hoursToNextShift: Long? = null,
    val todayShifts: List<ShiftDisplayModel> = emptyList(),
    val weekSchedule: List<LocalDate> = emptyList(),
    val shiftsThisWeek: List<Shift> = emptyList(),
    val isLoading: Boolean = true
)


data class ShiftDisplayModel(
    val shift: Shift,
    val workerNames: List<String>
)