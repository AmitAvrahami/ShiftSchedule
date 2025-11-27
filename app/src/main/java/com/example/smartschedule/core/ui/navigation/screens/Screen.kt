package com.example.smartschedule.core.ui.navigation.screens

sealed class Screen(val route : String) {
    object Login : Screen("login")
    object Schedule : Screen("schedule")
    object ManagerDashboard : Screen("manager_dashboard")
    object EmployeeDashboard : Screen("employee_dashboard")
    object AdminDashboard : Screen("admin_dashboard")

    object EmployeeScheduleView : Screen("employee_schedule_view")

    object Messages : Screen("messages")
    object Settings : Screen("settings")
    object ScheduleEditor : Screen("schedule_editor")
    object ManagerRequests : Screen("manager_requests")
    object ManagerMore : Screen("manager_more")
}
