package com.example.smartschedule.feature.smartSchedule.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.smartschedule.core.domain.model.employees.Employee
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.Shift
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftAssignment
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftId
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.RuleViolation
import com.example.smartschedule.feature.smartSchedule.ui.ScheduleUiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeeklyScheduleGrid(
    state: ScheduleUiState.Success,
    onShiftClick: (Shift) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        state.daysInRange.forEach { date ->
            DayColumn(
                date = date,
                shifts = state.shiftsByDate[date] ?: emptyList(),
                employeesById = state.employeesById,
                assignmentsByShiftId =state.assignmentsByShiftId,
                violationsByShiftId = state.violationsByShiftId,
                onShiftClick = onShiftClick
            )

            if (date != state.daysInRange.last()) {
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp),
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun DayColumn(
    date: LocalDate,
    shifts: List<Shift>,
    employeesById: Map<EmployeeId , Employee>,
    assignmentsByShiftId: Map<ShiftId , List<ShiftAssignment>>,
    violationsByShiftId: Map<ShiftId?, List<RuleViolation>>,
    onShiftClick: (Shift) -> Unit
) {
    val isToday = date == LocalDate.now()

    Column(
        modifier = Modifier
            .width(160.dp)
            .fillMaxHeight()
            .background(
                if (isToday) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("he")), // "יום א'"
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = date.format(DateTimeFormatter.ofPattern("dd/MM")), // "23/11"
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        if (shifts.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("-", color = Color.LightGray)
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                shifts.sortedBy { it.startTime }.forEach { shift ->

                    // שליפת העובדים המשובצים
                    val assignments = assignmentsByShiftId[shift.id] ?: emptyList()
                    val assignedEmployees = assignments.mapNotNull { employeesById[it.employeeId] }

                    // שליפת שגיאות רלוונטיות
                    val shiftViolations = violationsByShiftId[shift.id] ?: emptyList()

                    ShiftCard(
                        shift = shift,
                        assignedEmployees = assignedEmployees,
                        violations = shiftViolations,
                        onClick = { onShiftClick(shift) }
                    )
                }
            }
        }
    }
}

fun generateDateList(start: LocalDate, end: LocalDate): List<LocalDate> {
    val list = mutableListOf<LocalDate>()
    var current = start
    while (!current.isAfter(end)) {
        list.add(current)
        current = current.plusDays(1)
    }
    return list
}