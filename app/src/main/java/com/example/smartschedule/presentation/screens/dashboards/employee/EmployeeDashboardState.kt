package com.example.smartschedule.presentation.screens.dashboards.employee

import com.example.smartschedule.domain.models.shift.Shift
import com.example.smartschedule.domain.models.user.User
import java.time.LocalDate

typealias Message = String

data class EmployeeDashboardState(
    val user: User? = null,
    val employeeName: String = "",
    val employeeRole: String = "",
    val nextShift: Shift? = null,
    val todayShifts: List<Shift> = emptyList(),
    val weeklyShifts: List<Shift> = emptyList(),
    val managerMessages: List<Message> = emptyList(),
    val selectedDay: LocalDate = LocalDate.now(),
    val quickActions: List<DashboardAction> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
