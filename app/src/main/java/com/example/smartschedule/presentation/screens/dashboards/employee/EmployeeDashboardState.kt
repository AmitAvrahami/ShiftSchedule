package com.example.smartschedule.presentation.screens.dashboards.employee

import com.example.smartschedule.domain.models.shiftassignment.Assignment
import com.example.smartschedule.domain.models.user.User
import com.example.smartschedule.domain.models.workschedule.WorkSchedule
import java.time.LocalDate

typealias Message = String

data class EmployeeDashboardState(
    val user : User? = null ,
//    val employeeRole : EmployeeRole = user?.role as EmployeeRole ,
    val schedule : WorkSchedule? = null ,
    val nextAssignment : Assignment? = null ,
    val todayAssignments : List<Assignment> = emptyList() ,
    val weeklyShifts : List<Assignment> = emptyList() ,
    val managerMessages : List<Message> = emptyList() ,
    val quickActions : List<DashboardAction> = emptyList() ,
    val isLoading : Boolean = false ,
    val errorMessage : String? = null ,
    val dayDateMap : Map<String , String> = emptyMap() ,
    val workingDatesMap : Map<String , Boolean> = emptyMap() ,
    val nextShiftCountdown : Pair<String, String>? = null ,
)
