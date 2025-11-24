package com.example.smartschedule.feature.smartSchedule.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartschedule.core.domain.model.employees.Employee
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.employees.enums.EmployeeRole
import com.example.smartschedule.core.domain.model.employees.enums.EmploymentType
import com.example.smartschedule.core.domain.model.smartSchedule.Shift
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftId
import com.example.smartschedule.core.domain.model.smartSchedule.enums.ShiftType
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.RuleViolation
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.ViolationSeverity
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun ShiftCard(
    shift: Shift,
    assignedEmployees: List<Employee>, // רשימת העובדים המשובצים
    violations: List<RuleViolation>,   // רשימת שגיאות ספציפיות למשמרת זו
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. חישוב סטטוס השגיאה (הכי חמור מנצח)
    val maxSeverity = remember(violations) {
        when {
            violations.any { it.severity == ViolationSeverity.ERROR } -> ViolationSeverity.ERROR
            violations.any { it.severity == ViolationSeverity.WARNING } -> ViolationSeverity.WARNING
            else -> null
        }
    }

    // 2. קביעת צבע המסגרת לפי השגיאה
    val borderColor = when (maxSeverity) {
        ViolationSeverity.ERROR -> MaterialTheme.colorScheme.error
        ViolationSeverity.WARNING -> Color(0xFF463500) // Amber/Yellow
        null -> Color.Transparent
    }

    val borderStroke = if (maxSeverity != null) BorderStroke(2.dp, borderColor) else null

    // 3. עיצוב הכרטיס
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp) // גובה קבוע כדי שהגריד ייראה מסודר
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        border = borderStroke,
        colors = CardDefaults.cardColors(
            containerColor = getShiftColor(shift.shiftType) // צבע רקע לפי סוג המשמרת
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatTimeRange(shift.startTime, shift.endTime),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )

                if (maxSeverity != null) {
                    Icon(
                        imageVector = if (maxSeverity == ViolationSeverity.ERROR) Icons.Rounded.ErrorOutline else Icons.Default.Warning,
                        contentDescription = "Violation",
                        tint = borderColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (assignedEmployees.isEmpty()) {
                    Text(
                        text = "לא משובץ",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray.copy(alpha = 0.7f),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                } else {
                    assignedEmployees.take(2).forEach { employee ->
                        EmployeeChip(employee)
                    }
                    if (assignedEmployees.size > 2) {
                        Text(
                            text = "+ עוד ${assignedEmployees.size - 2}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            // --- תחתית: סוג משמרת ---
            Text(
                text = shift.shiftType.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = Color.DarkGray.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

// רכיב קטן להצגת שם העובד
@Composable
fun EmployeeChip(employee: Employee) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(Color.Gray) // בעתיד נשתמש ב-employee.colorHex
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = employee.fullName,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// --- פונקציות עזר ועיצוב ---

fun formatTimeRange(start: LocalTime, end: LocalTime): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return "${start.format(formatter)} - ${end.format(formatter)}"
}

// פונקציה לבחירת צבע רקע עדין לפי סוג המשמרת
fun getShiftColor(type: ShiftType): Color {
    return when (type) {
        ShiftType.MORNING -> Color(0xFFFFF9C4) // צהוב בהיר
        ShiftType.NOON -> Color(0xFFFFE0B2) // כתום בהיר
        ShiftType.NIGHT -> Color(0xFFC5CAE9)   // כחול בהיר
        else -> Color(0xFFF5F5F5)              // אפור
    }
}

// --- Preview כדי לראות את התוצאה בסטודיו ---
@Preview
@Composable
fun ShiftCardPreview() {
    val sampleShift = Shift(
        id = ShiftId(1),
        date = LocalDate.now(),
        startTime = LocalTime.of(8, 0),
        endTime = LocalTime.of(16, 0),
        shiftType = ShiftType.MORNING,
        notes = null
    )

    val sampleEmployee = Employee(
        id = EmployeeId(1) ,
        fullName = "דני כהן" ,
        role = EmployeeRole.EMPLOYEE ,
        isActive = true ,
        preferredShiftTypeIds = emptyList() ,
        employmentType = EmploymentType.FULL_TIME
    )

    val violation = RuleViolation(
        ruleId = "1", severity = ViolationSeverity.ERROR, message = "התנגשות",
        relatedShiftId = sampleShift.id, relatedEmployeeId = sampleEmployee.id
    )

    MaterialTheme {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // כרטיס תקין
            ShiftCard(
                shift = sampleShift,
                assignedEmployees = listOf(sampleEmployee),
                violations = emptyList(),
                onClick = {},
                modifier = Modifier.width(120.dp)
            )

            // כרטיס עם שגיאה (מסגרת אדומה)
            ShiftCard(
                shift = sampleShift.copy(shiftType = ShiftType.NIGHT),
                assignedEmployees = listOf(sampleEmployee, sampleEmployee),
                violations = listOf(violation),
                onClick = {},
                modifier = Modifier.width(120.dp)
            )
        }
    }
}