package com.example.smartschedule.presentation.screens.dashboards.employee

import androidx.compose.material.icons.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class DashboardAction(
    val title: String,
    val icon: ImageVector
) {
    data object ViewSchedule : DashboardAction("סידור עבודה", Icons.Default.CalendarToday)
    data object RequestShiftSwap : DashboardAction("בקשת החלפה", Icons.AutoMirrored.Filled.CompareArrows)
    data object RequestTimeOff : DashboardAction("בקשת חופש", Icons.Default.EventBusy)
    data object ViewWeeklyShifts : DashboardAction("משמרות שבוע", Icons.Default.DateRange)
    data object ViewMessages : DashboardAction("הודעות מנהל", Icons.AutoMirrored.Filled.Message)
}
