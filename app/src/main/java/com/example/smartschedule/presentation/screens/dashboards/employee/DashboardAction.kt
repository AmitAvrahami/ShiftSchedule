package com.example.smartschedule.presentation.screens.dashboards.employee

import androidx.compose.material.icons.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class DashboardAction(
) {
    data object ViewSchedule : DashboardAction()
    data object RequestShiftSwap : DashboardAction()
    data object ContactManager : DashboardAction()
    object LoadData : DashboardAction()
}

sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
}
