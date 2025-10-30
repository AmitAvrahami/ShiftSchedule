package com.example.smartschedule.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartschedule.presentation.screens.auth.login.LoginScreen
import com.example.smartschedule.presentation.screens.auth.signup.SignUpScreen

@Composable
fun AppNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ){
        composable(Screen.Login.route){
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route)
                },
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }
        composable(Screen.SignUp.route){
            SignUpScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Home.route)
                },
            )
        }
    }
}