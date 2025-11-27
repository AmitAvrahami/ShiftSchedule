package com.example.smartschedule.core.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.smartschedule.core.domain.model.employees.enums.EmployeeRole
import com.example.smartschedule.core.ui.components.AppBottomBar
import com.example.smartschedule.core.ui.navigation.AppNavGraph
import com.example.smartschedule.core.ui.navigation.ManagerBottomNavItems
import com.example.smartschedule.core.ui.navigation.WorkerBottomNavItems
import com.example.smartschedule.core.ui.navigation.screens.Screen

@Composable
fun MainAppScreen(
    startDestination: String,
     userRole: EmployeeRole?
){
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute != Screen.Login.route
    Scaffold(
        bottomBar = {
            if (showBottomBar && userRole != null){
                val items = if (userRole == EmployeeRole.MANAGER || userRole == EmployeeRole.ADMIN) {
                    ManagerBottomNavItems
                } else {
                    WorkerBottomNavItems
                }
                AppBottomBar(navController, items)
            }
        }
    ) { innerPadding ->
        AppNavGraph(
            navController = navController ,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        )
    }
}