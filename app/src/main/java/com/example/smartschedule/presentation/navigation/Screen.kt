package com.example.smartschedule.presentation.navigation

import androidx.navigation.NavHostController
import com.example.smartschedule.domain.models.user.roles.Roles


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object EmployeeDashBoard : Screen("employee_dashboard")
    object ManagerDashBoard : Screen("manager_dashboard")

}

fun NavHostController.navigateToDashboard(role: Roles){
    when(role) {
        Roles.EMPLOYEE -> navigate(Screen.EmployeeDashBoard.route)
        Roles.MANAGER -> navigate(Screen.ManagerDashBoard.route)
        Roles.ADMIN -> navigate(Screen.ManagerDashBoard.route)
    }
}