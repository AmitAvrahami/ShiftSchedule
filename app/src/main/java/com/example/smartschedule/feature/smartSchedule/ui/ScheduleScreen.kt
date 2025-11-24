package com.example.smartschedule.feature.smartSchedule.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartschedule.core.domain.model.smartSchedule.Shift
import com.example.smartschedule.feature.smartSchedule.ui.components.AssignmentSheet
import com.example.smartschedule.feature.smartSchedule.ui.components.WeeklyScheduleGrid

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is ScheduleUiState.Loading -> CircularProgressIndicator()
                is ScheduleUiState.Error -> Text("שגיאה: ${state.message}")

                is ScheduleUiState.Success -> {
                    WeeklyScheduleGrid(
                        state = state,
                        onShiftClick = { shift ->
                            viewModel.toggleSelectedShift(shift)
                        }
                    )

                    if (state.selectedShift!= null) {
                        AssignmentSheet(
                            shift = state.selectedShift,
                            allEmployees = state.employees,
                            currentAssignments = state.schedule.assignments,
                            onDismiss = { viewModel.clearSelection() },
                            onToggleEmployee = { employeeId ->
                                viewModel.toggleEmployeeAssignment(state.selectedShift.id, employeeId)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleDebugContent(state: ScheduleUiState.Success) {
    Column {
        Text(
            text = "לוח משמרות: ${state.schedule.name}",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(text = "מספר משמרות: ${state.schedule.shifts.size}")
        Text(text = "מספר שיבוצים: ${state.schedule.assignments.size}")

        Text(
            text = "🔴 חריגות שנמצאו: ${state.violations.size}",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleMedium
        )

        state.violations.forEach { violation ->
            Text(text = "- ${violation.message}")
        }
    }
}