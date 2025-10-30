package com.example.smartschedule.presentation.screens.dashboards.employee

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartschedule.R
import com.example.smartschedule.ui.theme.SmartScheduleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDashboardScreen() {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavigationBar() }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { NextShiftCard() }
            item { TodaysShiftsSection() }
            item { MyWeekSection() }
            item { ManagerAnnouncementsSection() }
            item { QuickActionsSection() }
            item { QuoteOfTheDayCard() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    CenterAlignedTopAppBar(
        title = { Text("Good morning, Brooklyn 👋", style = MaterialTheme.typography.titleMedium) },
        navigationIcon = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Profile",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun NextShiftCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Next shift in 2 hours", color = Color.White, style = MaterialTheme.typography.titleLarge)
                Text("10:00 AM - 6:00 PM |", color = Color.White.copy(alpha = 0.8f))
                Text("Cashier", color = Color.White.copy(alpha = 0.8f))
                Text("Main St. Branch", color = Color.White.copy(alpha = 0.8f))
            }
            Button(
                onClick = { /* TODO */ },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Det...", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun TodaysShiftsSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Today's shifts", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(2) { index ->
                ShiftCard(index)
            }
        }
    }
}

@Composable
fun ShiftCard(index: Int) {
    Card(
        modifier = Modifier.width(180.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = if (index == 0) R.drawable.ic_launcher_background else R.drawable.ic_launcher_background),
                contentDescription = "Store",
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text("10:00 - 18:00", fontWeight = FontWeight.Bold)
                Text("Cashier @ Main St", fontSize = 14.sp)
                Text(if (index == 0) "Downtown" else "Enjoy", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun MyWeekSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("My Week", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeekDay("MON", "12", false)
                WeekDay("TUE", "13", true)
                WeekDay("WED", "14", false, hasShift = true)
                WeekDay("THU", "15", false)
                WeekDay("FRI", "16", false, hasShift = true)
                WeekDay("SAT", "17", false)
                WeekDay("SUN", "18", false, hasShift = true)
            }
        }
    }
}

@Composable
fun WeekDay(day: String, date: String, isSelected: Boolean, hasShift: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(day, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(date, color = if (isSelected) Color.White else Color.Black)
        }
        if (hasShift) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
fun ManagerAnnouncementsSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Manager Announcements", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("New Holiday Schedule", fontWeight = FontWeight.Bold)
                Text("Please review the updated schedule for the upcoming holiday season...", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Posted on July 13", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun QuickActionsSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Quick Actions", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                QuickActionItem(icon = Icons.Default.SwapHoriz, text = "Request Change")
                QuickActionItem(icon = Icons.Default.Chat, text = "Contact Manager")
                QuickActionItem(icon = Icons.Default.CalendarToday, text = "View Schedule")
            }
        }
    }
}

@Composable
fun QuickActionItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = text, tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text, fontSize = 12.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun QuoteOfTheDayCard() {
    Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("\"", fontSize = 32.sp, color = MaterialTheme.colorScheme.primary)
            Text(
                "The only way to do great work is to love what you do.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("- Steve Jobs", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}


@Composable
fun BottomNavigationBar() {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            selected = true,
            onClick = { /* TODO */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.CalendarToday, contentDescription = "My Shifts") },
            label = { Text("My Shifts") },
            selected = false,
            onClick = { /* TODO */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Chat, contentDescription = "Messages") },
            label = { Text("Messages") },
            selected = false,
            onClick = { /* TODO */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = false,
            onClick = { /* TODO */ }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun EmployeeDashboardScreenPreview() {
    SmartScheduleTheme {
        EmployeeDashboardScreen()
    }
}
