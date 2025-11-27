package com.example.smartschedule.feature.employees.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartschedule.core.domain.model.employees.Employee
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.employees.enums.EmployeeRole
import com.example.smartschedule.core.domain.model.employees.enums.EmploymentType
import com.example.smartschedule.core.domain.model.smartSchedule.Shift
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftId
import com.example.smartschedule.core.domain.model.smartSchedule.enums.ShiftType
import com.example.smartschedule.feature.employees.ui.components.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun EmployeeDashboardScreen(
    viewModel: EmployeeDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EmployeeDashboardContent(uiState = uiState)
}

@Composable
fun EmployeeDashboardContent(
    uiState: EmployeeUiState
) {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Color(0xFFF5F6FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {

            // --- 1. Header (כותרת ופרופיל) ---
            Spacer(modifier = Modifier.height(16.dp))
            EmployeeDashboardHeader(employeeName = uiState.currentEmployee?.fullName ?: "")

            // --- 2. Next Shift (המשמרת הבאה) ---
            Spacer(modifier = Modifier.height(24.dp))
            NextShiftCard(
                shift = uiState.nextShift,
                hoursUntil = uiState.hoursToNextShift,
                onDetailsClick = { }
            )

            // --- 3. Today Shifts (משמרות להיום) ---
            Spacer(modifier = Modifier.height(24.dp))
            Text("משמרות להיום", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            TodayShiftCards(uiState.todayShifts)

            // --- 4. My Week (השבוע שלי - היה חסר לך!) ---
            Spacer(modifier = Modifier.height(24.dp))
            MyWeekSection(uiState)

            // --- 5. Announcements (הודעות מנהל) ---
            Spacer(modifier = Modifier.height(24.dp))
            Text("הודעות מנהל", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            AnnouncementCard()

            // --- 6. Quick Actions (פעולות מהירות - היה חסר לך!) ---
            Spacer(modifier = Modifier.height(24.dp))
            Text("פעולות מהירות", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            QuickActionItems()

            // --- 7. Quote (ציטוט - היה חסר לך!) ---
            Spacer(modifier = Modifier.height(24.dp))
            QuoteSection()

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun QuoteSection() {
    Card(
        modifier = Modifier.fillMaxWidth() ,
        colors = CardDefaults.cardColors(containerColor = Color.White) ,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp) ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "99" ,
                fontSize = 40.sp ,
                color = Color(0xFF42A5F5) ,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "\"הדרך היחידה לעשות עבודה נהדרת היא לאהוב את מה שאתה עושה.\"" ,
                textAlign = TextAlign.Center ,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "- סטיב ג'ובס" ,
                style = MaterialTheme.typography.bodySmall ,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun QuickActionItems() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White) ,
        shape = RoundedCornerShape(16.dp) ,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) ,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            QuickActionItem(
                icon = Icons.Outlined.CalendarToday ,
                label = "צפייה בלו\"ז"
            ) { }
            QuickActionItem(
                icon = Icons.Outlined.Chat ,
                label = "יצירת קשר"
            ) { }
            QuickActionItem(
                icon = Icons.Outlined.SwapHoriz ,
                label = "בקשת שינוי"
            ) { }
        }
    }
}

@Composable
private fun MyWeekSection(uiState : EmployeeUiState) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White) ,
        shape = RoundedCornerShape(16.dp) ,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "השבוע שלי" ,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth() ,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                uiState.weekSchedule.forEach { date ->
                    val isToday = date == LocalDate.now()
                    // בדיקה האם יש לי משמרת בתאריך הזה
                    val hasShift = uiState.shiftsThisWeek.any { it.date == date }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = date.format(
                                DateTimeFormatter.ofPattern(
                                    "EE" ,
                                    Locale("he")
                                )
                            ) ,
                            style = MaterialTheme.typography.bodySmall ,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isToday) Color(0xFFE3F2FD) else Color.Transparent) ,
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = date.dayOfMonth.toString() ,
                                    fontWeight = FontWeight.Bold ,
                                    color = if (isToday) Color(0xFF1976D2) else Color.Black
                                )
                                // נקודה כחולה אם יש משמרת
                                if (hasShift) {
                                    Box(
                                        modifier = Modifier
                                            .size(4.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF1976D2))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmployeeDashboardHeader(
    employeeName: String = ""
) {
    Row(
        modifier = Modifier.fillMaxWidth() ,
        verticalAlignment = Alignment.CenterVertically ,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MenuButton()

        GreetingTitle(employeeName)

        Avatar(employeeName.take(2))
    }
}


@Composable
private fun Avatar(
    letters: String = ""
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color(0xFFBBDEFB)) ,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text =letters ,
            fontWeight = FontWeight.Bold ,
            color = Color(0xFF1976D2)
        )
    }
}

@Composable
private fun GreetingTitle(
    employeeName: String? = null
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val firstName = employeeName ?: "אורח"
        Text(
            text = "בוקר טוב, $firstName" ,
            style = MaterialTheme.typography.titleLarge ,
            fontWeight = FontWeight.Bold
        )
        Text(
            " 👋" ,
            fontSize = 24.sp
        )
    }
}

@Composable
private fun MenuButton() {
    IconButton(onClick = { }) {
        Icon(
            imageVector = Icons.Default.Menu ,
            contentDescription = "Menu"
        )
    }
}

@Composable
fun TodayShiftCards(todayShifts: List<ShiftDisplayModel>) {
    LazyRow {
        items(todayShifts) { shiftDisplay ->
            TodayShiftCard(model = shiftDisplay)
        }
    }
}

@Preview(showBackground = true, locale = "he" ,
    device = "spec:width=1080px,height=5000px"
)
@Composable
fun EmployeeDashboardPreview() {
    // 1. יצירת נתוני דמה (Mock Data) לתצוגה
    val mockEmployee = Employee(
        id = EmployeeId(1) ,
        fullName = "ישראל ישראלי" ,
        role = EmployeeRole.EMPLOYEE ,
        isActive = true ,
        preferredShiftTypeIds = emptyList() ,
        employmentType = EmploymentType.FULL_TIME
    )

    val mockShift = Shift(
        id = ShiftId(1) ,
        date = LocalDate.now().plusDays(1) ,
        startTime = LocalTime.of(
            10 ,
            0
        ) ,
        endTime = LocalTime.of(
            18 ,
            0
        ) ,
        shiftType = ShiftType.MORNING ,
        notes = null
    )

    val todayMockShift = ShiftDisplayModel(
        shift = mockShift.copy(startTime = java.time.LocalTime.of(8,0), endTime = java.time.LocalTime.of(16,0)),
        workerNames = listOf("דני", "יוסי", "רוני")
    )

    // 2. בניית ה-State המזויף
    val mockState = EmployeeUiState(
        currentEmployee = mockEmployee,
        nextShift = mockShift,
        hoursToNextShift = 24,
        todayShifts = listOf(todayMockShift, todayMockShift.copy(workerNames = listOf("מיכל"))),
        weekSchedule = (0..6).map { LocalDate.now().plusDays(it.toLong()) },
        shiftsThisWeek = listOf(mockShift),
        isLoading = false
    )

    // 3. הצגת ה-Content עם הנתונים
    MaterialTheme {
        // בגלל שזה עברית, כדאי לעטוף ב-LayoutDirection אם הטקסט לא מתיישר,
        // אבל ה-locale="he" באנוטציה למעלה אמור לטפל ברוב המקרים.
        EmployeeDashboardContent(uiState = mockState)
    }
}