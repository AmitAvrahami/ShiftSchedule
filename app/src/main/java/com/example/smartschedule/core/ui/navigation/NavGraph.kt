package com.example.smartschedule.core.ui.navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartschedule.core.ui.navigation.screens.Screen
import com.example.smartschedule.feature.auth.ui.LoginScreen
import com.example.smartschedule.feature.employees.ui.EmployeeDashboardScreen
import com.example.smartschedule.feature.smartSchedule.ui.ScheduleScreen

@Composable
fun AppNavGraph(
    navController : NavHostController ,
    startDestination : String ,
    modifier : Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
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
        composable(Screen.Messages.route) { PlaceholderScreen("הודעות") }
        composable(Screen.Settings.route) { PlaceholderScreen("הגדרות") }
        composable(Screen.EmployeeScheduleView.route) { PlaceholderScreen("צפייה בסידור") }

        // מנהל
        composable(Screen.ScheduleEditor.route) { PlaceholderScreen("עורך סידור") }
        composable(Screen.ManagerRequests.route) { PlaceholderScreen("בקשות עובדים") }
        composable(Screen.ManagerMore.route) { PlaceholderScreen("עוד...") }
    }
}


@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(text = "מסך $title בבנייה 🚧")
    }
}

