package com.example.smartschedule.core.ui.navigation.screens

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Schedule : Screen("schedule")
    object ManagerDashboard: Screen("manager_dashboard")
    object EmployeeDashboard: Screen("employee_dashboard")
    object AdminDashboard: Screen("admin_dashboard")
}
