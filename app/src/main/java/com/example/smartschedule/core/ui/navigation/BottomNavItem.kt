package com.example.smartschedule.core.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.smartschedule.core.ui.navigation.screens.Screen

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

// רשימת הכפתורים לעובד
val WorkerBottomNavItems = listOf(
    BottomNavItem(
        route = Screen.EmployeeDashboard.route,
        label = "ראשי",
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    ),
    BottomNavItem(
        route = Screen.EmployeeScheduleView.route,
        label = "סידור",
        icon = Icons.Outlined.CalendarMonth,
        selectedIcon = Icons.Filled.CalendarMonth
    ),
    BottomNavItem(
        route = Screen.Messages.route,
        label = "הודעות",
        icon = Icons.Outlined.Chat,
        selectedIcon = Icons.Filled.Chat
    ),
    BottomNavItem(
        route = Screen.Settings.route,
        label = "הגדרות",
        icon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings
    )
)

// רשימת הכפתורים למנהל
val ManagerBottomNavItems = listOf(
    BottomNavItem(
        route = Screen.ManagerDashboard.route,
        label = "דשבורד",
        icon = Icons.Outlined.Dashboard,
        selectedIcon = Icons.Filled.Dashboard
    ),
    BottomNavItem(
        route = "manager_employees",
        label = "עובדים",
        icon = Icons.Outlined.Group,
        selectedIcon = Icons.Filled.Group
    ),
    BottomNavItem(
        route = Screen.ScheduleEditor.route,
        label = "סידור",
        icon = Icons.Outlined.EditCalendar,
        selectedIcon = Icons.Filled.EditCalendar
    ),
    BottomNavItem(
        route = Screen.ManagerRequests.route,
        label = "בקשות",
        icon = Icons.Outlined.SwapHoriz,
        selectedIcon = Icons.Filled.SwapHoriz
    ),
    BottomNavItem(
        route = Screen.ManagerMore.route,
        label = "עוד",
        icon = Icons.Outlined.Menu,
        selectedIcon = Icons.Filled.Menu
    )
)