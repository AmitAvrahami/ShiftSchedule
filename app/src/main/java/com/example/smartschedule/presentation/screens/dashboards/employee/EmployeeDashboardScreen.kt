package com.example.smartschedule.presentation.screens.dashboards.employee

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.smartschedule.R
import com.example.smartschedule.domain.models.shift.AfterNoonShiftStrategy
import com.example.smartschedule.domain.models.shift.MorningShiftStrategy
import com.example.smartschedule.domain.models.shift.NightShiftStrategy
import com.example.smartschedule.domain.models.shift.Shift
import com.example.smartschedule.domain.models.shiftassignment.Assignment
import com.example.smartschedule.domain.models.shiftassignment.AssignmentStatus
import com.example.smartschedule.domain.models.user.User
import com.example.smartschedule.domain.models.user.roles.EmployeeRole
import com.example.smartschedule.presentation.viewmodel.dashboard.EmployeeDashboardViewModel
import com.example.smartschedule.ui.theme.SmartScheduleTheme
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale.getDefault
import kotlin.coroutines.cancellation.CancellationException


@Composable
fun EmployeeDashboardScreen(
    navController : NavController,
    viewModel : EmployeeDashboardViewModel = hiltViewModel(),
) {
    val state = viewModel.state


    LaunchedEffect(key1 = true) {
        try {
            while (isActive) {
                viewModel.onDashboardAction(DashboardAction.LoadData)
                delay(60_000L)
                Log.d("EmployeeDashboardScreen", "Refreshing data...")
            }
        } catch (e: CancellationException) {
            Log.d("EmployeeDashboardScreen", "Refresh stopped (screen left)")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> navController.navigate(event.route)
            }
        }
    }


    Scaffold(
        topBar = { TopBar(employeeName = state.user?.fullName) } ,
        bottomBar = { BottomNavigationBar() }) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            when {
                state.isLoading -> LoadingDashboardSkeleton()
                state.errorMessage != null -> ErrorState(
                    message = state.errorMessage ,
                    onRetry = { viewModel.onDashboardAction(DashboardAction.LoadData) })

                else -> DashboardContent(state = state,viewModel::onDashboardAction)
            }
        }
    }
}

@Composable
fun DashboardContent(
    state : EmployeeDashboardState ,
    onDashboardAction : (DashboardAction) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp) ,
        contentPadding = PaddingValues(vertical = 16.dp) ,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            NextShiftCard(
                state.nextAssignment?.shift ,
                state.nextShiftCountdown?.first ,
                state.nextShiftCountdown?.second
            )
        }
        item {
            TodayShiftsSection(
                todayAssignments = state.todayAssignments
            )
        }
        item {
            MyWeekSection(
                shiftDaysMap = state.workingDatesMap ,
                state.dayDateMap
            )
        }
        item { ManagerAnnouncementsSection(state.managerMessages) }
        item { QuickActionsSection(onDashboardAction) }
        item { QuoteOfTheDayCard() }
    }
}

@Composable
fun LoadingDashboardSkeleton(
    itemCount : Int = 5 ,
    modifier : Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp) ,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(itemCount) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmer()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            )
        }
    }
}

@Composable
fun ErrorState(
    message : String ,
    onRetry : () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp) ,
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Error ,
            contentDescription = null ,
            tint = MaterialTheme.colorScheme.error ,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message ,
            style = MaterialTheme.typography.bodyLarge ,
            textAlign = TextAlign.Center ,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(employeeName : String?) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Good morning, ${employeeName ?: "Employee"} 👋" ,
                style = MaterialTheme.typography.titleMedium
            )
        } ,
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(
                    Icons.Default.Menu ,
                    contentDescription = "Menu"
                )
            }
        } ,
        actions = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        } ,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
fun NextShiftCard(
    nextShift : Shift? ,
    countdownValue : String? ,
    countdownUnit : String? ,
) {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    Card(
        modifier = Modifier.fillMaxWidth() ,
        shape = RoundedCornerShape(16.dp) ,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.padding(16.dp) ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                if (nextShift != null) {
                    if (countdownValue != null && countdownUnit != null) {
                        Text(
                            "Next shift in $countdownValue $countdownUnit" ,
                            color = Color.White ,
                            style = MaterialTheme.typography.titleLarge
                        )
                    } else {
                        Text(
                            "Next shift soon" ,
                            color = Color.White ,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Text(
                        "${nextShift.startDate.format(formatter)} - ${
                            nextShift.endDate.format(
                                formatter
                            )
                        }" ,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        nextShift.strategy.getShiftStrategyType().displayName ,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                } else {
                    Text(
                        "No upcoming shifts" ,
                        color = Color.White ,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        "Enjoy your day off!" ,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Button(
                onClick = { /* TODO */ } ,
                shape = RoundedCornerShape(12.dp) ,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White) ,
                enabled = nextShift != null
            ) {
                Text(
                    "Details" ,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@Composable
fun TodayShiftsSection(todayAssignments : List<Assignment>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Today's shifts" ,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (todayAssignments.isNotEmpty()) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(todayAssignments) { assignment ->
                    ShiftCard(assignment)
                }
            }
        } else {
            Text(
                "No shifts scheduled for today." ,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ShiftCard(assignment : Assignment , employeeNames : List<String> = emptyList()) {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val shift = assignment.shift
    val employeeNames = assignment.assignedEmployees //TODO: Replace with actual employee names

    Card(
        modifier = Modifier.width(180.dp) ,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            val imageRes = when (assignment.shift.strategy) {
                is MorningShiftStrategy -> R.drawable.ic_morning_shift
                is AfterNoonShiftStrategy -> R.drawable.ic_afternoon_shift
                is NightShiftStrategy -> R.drawable.ic_night_shift
                else -> R.drawable.ic_launcher_foreground // Default image
            }
            Image(
                painter = painterResource(id = imageRes) ,
                contentDescription = shift.strategy.getShiftStrategyType().displayName ,
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally) ,
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    "${shift.startDate.format(formatter)} - ${shift.endDate.format(formatter)}" ,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    shift.strategy.getShiftStrategyType().displayName ,
                    color = Color.Gray
                )
                Text(
                    "Employees: ${employeeNames.joinToString(", ")}" ,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun MyWeekSection(
    shiftDaysMap : Map<String , Boolean> ,
    dayDateMap : Map<String , String>
) {
    val today = LocalDate.now().dayOfWeek.name
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "My Week" ,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(16.dp) ,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp) ,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                dayDateMap.forEach { (day , date) ->
                    WeekDay(
                        day = day.uppercase(getDefault()).take(3) ,
                        date = date ,
                        isSelected = day == today ,
                        hasShift = shiftDaysMap[date] == true
                    )
                }
            }
        }
    }
}

@Composable
fun WeekDay(
    day : String ,
    date : String ,
    isSelected : Boolean ,
    hasShift : Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            day ,
            fontSize = MaterialTheme.typography.labelSmall.fontSize ,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent) ,
            contentAlignment = Alignment.Center
        ) {
            Text(
                date ,
                color = if (isSelected) Color.White else Color.Black
            )
        }
        if (hasShift) {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
fun ManagerAnnouncementsSection(messages : List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Manager Announcements" ,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                Icons.Default.Notifications ,
                contentDescription = null
            )
        }
        if (messages.isEmpty()) {
            Text(
                "No new announcements." ,
                color = Color.Gray
            )
        } else {
            messages.forEach { msg ->
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(16.dp) ,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            msg ,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionsSection(onDashboardAction : (DashboardAction) -> Unit = {}) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Quick Actions" ,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
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
                    Icons.Default.SwapHoriz ,
                    "Request Change" ,
                    onClick = { onDashboardAction(DashboardAction.RequestShiftSwap) }
                )
                QuickActionItem(
                    Icons.Default.Chat ,
                    "Contact Manager" ,
                    onClick = { onDashboardAction(DashboardAction.ContactManager) }
                )
                QuickActionItem(
                    Icons.Default.CalendarToday ,
                    "View Schedule" ,
                    onClick = { onDashboardAction(DashboardAction.ViewSchedule) }
                )
            }
        }
    }
}

@Composable
fun QuickActionItem(
    icon : ImageVector ,
    text : String ,
    onClick : () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally ,
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .clickable(onClick =  onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon ,
                contentDescription = text ,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text ,
            fontSize = MaterialTheme.typography.labelSmall.fontSize ,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun QuoteOfTheDayCard() {
    Card(
        shape = RoundedCornerShape(16.dp) ,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp) ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "“" ,
                fontSize = 32.sp ,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "The only way to do great work is to love what you do." ,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "- Steve Jobs" ,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Dashboard ,
                    null
                )
            } ,
            label = { Text("Dashboard") } ,
            selected = true ,
            onClick = { })
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.CalendarToday ,
                    null
                )
            } ,
            label = { Text("Schedule") } ,
            selected = false ,
            onClick = { })
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Chat ,
                    null
                )
            } ,
            label = { Text("Chat") } ,
            selected = false ,
            onClick = { })
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Settings ,
                    null
                )
            } ,
            label = { Text("Settings") } ,
            selected = false ,
            onClick = { })
    }
}
//
//@Preview(
//    showBackground = true ,
//    name = "Dashboard Preview" ,
//    heightDp = 1500 ,
//    showSystemUi = false ,
//)
//@Composable
//fun EmployeeDashboardPreview() {
//    val dummyUser = User(
//        id = "123" ,
//        fullName = "Amit" ,
//        nationalId = "123456789" ,
//        role = EmployeeRole(hireDate = LocalDate.now())
//    )
//    val dummyShift1 = Shift(
//        id = "1" ,
//        startDate = LocalDateTime.now().withHour(9) ,
//        endDate = LocalDateTime.now().withHour(17) ,
//        strategy = MorningShiftStrategy()
//    )
//    val dummyShift2 = Shift(
//        id = "2" ,
//        startDate = LocalDateTime.now().withHour(17) ,
//        endDate = LocalDateTime.now().withHour(23) ,
//        strategy = AfterNoonShiftStrategy()
//    )
//    val dummyAssignment1 = Assignment(
//        id = 1L ,
//        assignedEmployees = listOf("123") ,
//        shift = dummyShift1 ,
//        assignmentStatus = AssignmentStatus.CANCELLED ,
//        assignedBy = EmployeeRole(
//            hireDate = LocalDate.now()
//        ) ,
//        assignedAt = LocalDateTime.now()
//    )
//    val dummyAssignment2 = Assignment(
//        id = 2L ,
//        assignedEmployees = listOf("123") ,
//        shift = dummyShift2 ,
//        assignmentStatus = AssignmentStatus.CONFIRMED ,
//        assignedBy = EmployeeRole(
//            hireDate = LocalDate.now() ,
//        ) ,
//        assignedAt = LocalDateTime.now()
//    )
//    val dummyState = EmployeeDashboardState(
//        user = dummyUser ,
//        todayAssignments = listOf(
//            dummyAssignment1 ,
//            dummyAssignment2
//        ) ,
//        nextAssignment = dummyAssignment1 ,
//        nextShiftCountdown = Pair(
//            "2" ,
//            "hours"
//        ) ,
//        workingDatesMap = mutableMapOf(
//            "23" to true ,
//            "24" to false ,
//            "25" to true ,
//            "26" to true ,
//            "27" to false ,
//            "28" to true ,
//            "29" to false
//        ) ,
//        dayDateMap = mutableMapOf(
//            "MON" to "23" ,
//            "TUE" to "24" ,
//            "WED" to "25" ,
//            "THU" to "26" ,
//            "FRI" to "27" ,
//            "SAT" to "28" ,
//            "SUN" to "29"
//        ) ,
//        managerMessages = listOf(
//            "New holiday schedule available." ,
//            "Team meeting this Friday at 3 PM."
//        ) ,
//        isLoading = false ,
//        errorMessage = null
//    )
//
//    SmartScheduleTheme {
//        EmployeeDashboardScreen(
//            state = dummyState ,
//        )
//    }
//}




// TODO(7): הוסף אנימציות מעבר קלות (fadeIn, animateContentSize) לטעינה או מעבר בין מצבים
// לדוגמה כשנטען schedule או כשיוצג error → Success UI


// TODO(10): הצג שם עובד/תמונה אמיתית בפרופיל ב־TopBar (state.user?.profileImage)


//CleanCode
// TODO(11): צור מודל אמיתי עבור הודעות מנהל (ManagerMessage)
// data class ManagerMessage(val title: String, val content: String, val date: String)
// והחלף את messages: List<String> ב־List<ManagerMessage>

// TODO(12): הוסף פונקציה ב־ShiftCard שממירה מזהי עובדים לשמות אמיתיים
// employeeRepository.getUserNameById(id)


//
//@Preview(
//    showBackground = true ,
//    name = "Loading State"
//)
//@Composable
//fun EmployeeDashboardLoadingPreview() {
//    SmartScheduleTheme {
//        EmployeeDashboardScreen(state = EmployeeDashboardState(isLoading = true))
//    }
//}
//
//@Preview(
//    showBackground = true ,
//    name = "Error State"
//)
//@Composable
//fun EmployeeDashboardErrorPreview() {
//    SmartScheduleTheme {
//        EmployeeDashboardScreen(
//            state = EmployeeDashboardState(
//                isLoading = false ,
//                errorMessage = "Could not connect to server. Please try again."
//            )
//        )
//    }
//}


