package com.example.smartschedule.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartschedule.domain.models.user.roles.Roles
import com.example.smartschedule.presentation.screens.auth.login.LoginScreen
import com.example.smartschedule.presentation.screens.auth.signup.SignUpScreen
import com.example.smartschedule.presentation.screens.dashboards.employee.EmployeeDashboardScreen
import com.example.smartschedule.presentation.screens.dashboards.manager.ManagerDashboardScreen

@Composable
fun AppNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ){
        composable(Screen.Login.route){
            LoginScreen(
                onLoginSuccess = { user ->
                    user?.let { user ->
                        navController.navigateToDashboard(user.role.getRole())
                    }
                },
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }
        composable(Screen.SignUp.route){
            SignUpScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) //TODO: change to login screen OR role dashboard
                },
            )
        }
        composable(Screen.ManagerDashBoard.route){
            ManagerDashboardScreen()
        }
        composable(Screen.EmployeeDashBoard.route){
            EmployeeDashboardScreen()
        }
    }
}