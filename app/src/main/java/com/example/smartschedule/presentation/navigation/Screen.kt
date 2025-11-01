package com.example.smartschedule.presentation.navigation


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object EmployeeDashBoard : Screen("employee_dashboard")
    object ManagerDashBoard : Screen("manager_dashboard")

}