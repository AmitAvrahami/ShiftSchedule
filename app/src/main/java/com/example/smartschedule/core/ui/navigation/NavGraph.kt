package com.example.smartschedule.core.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartschedule.core.ui.navigation.screens.Screen
import com.example.smartschedule.feature.auth.ui.LoginScreen
import com.example.smartschedule.feature.employees.ui.EmployeeDashboardScreen
import com.example.smartschedule.feature.smartSchedule.ui.ScheduleScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToManager = {
                    navController.navigate(Screen.ManagerDashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true } // מנקה היסטוריה
                    }
                } ,
                onNavigateToEmployee = {
                    navController.navigate(Screen.EmployeeDashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ManagerDashboard.route) {
            ScheduleScreen()
        }

        composable(Screen.EmployeeDashboard.route) {
            EmployeeDashboardScreen()
        }
    }
}