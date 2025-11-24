package com.example.smartschedule.feature.smartSchedule.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.smartschedule.core.domain.model.employees.Employee
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.Shift
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftAssignment
import com.example.smartschedule.core.domain.model.smartSchedule.enums.AssignmentStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentSheet(
    shift: Shift,
    allEmployees: List<Employee>,
    currentAssignments: List<ShiftAssignment>,
    onDismiss: () -> Unit,
    onToggleEmployee: (EmployeeId) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "שיבוץ עובדים למשמרת ${shift.shiftType.displayName}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                items(allEmployees) { employee ->
                    val isAssigned = remember(currentAssignments) {
                        currentAssignments.any {
                            it.employeeId == employee.id &&
                                    it.shiftId == shift.id &&
                                    it.status == AssignmentStatus.ACTIVE
                        }
                    }

                    EmployeeSelectionItem(
                        employee = employee,
                        isSelected = isAssigned,
                        onClick = { onToggleEmployee(employee.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun EmployeeSelectionItem(
    employee: Employee,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = employee.fullName,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = employee.role.name,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}