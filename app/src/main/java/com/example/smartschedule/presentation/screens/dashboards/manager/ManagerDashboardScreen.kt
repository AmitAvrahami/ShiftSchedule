package com.example.smartschedule.presentation.screens.dashboards.manager

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartschedule.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerDashboardScreen() {
    Scaffold(
        topBar = { ManagerDashboardTopAppBar() },
        bottomBar = { ManagerDashboardBottomBar() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Header()
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                OverviewCards()
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                ShiftOverview()
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                TeamStatus()
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                QuickActionsAndDistribution()
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                Announcements()
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerDashboardTopAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Welcome back, Jessica 👋",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Outlined.Dashboard, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun Header() {
    Text(
        "Tuesday, August 27",
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray
    )
}

@Composable
fun OverviewCards() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        InfoCard("128", "Total employees", Icons.Default.Groups)
        InfoCard("24", "Shifts today", Icons.Outlined.CalendarMonth)
        InfoCard("8", "Open requests", Icons.Outlined.Mail)
    }
}

@Composable
fun InfoCard(value: String, label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}

@Composable
fun ShiftOverview() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Shift Overview", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("View Full Schedule", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            FilterChip("By Role")
            FilterChip("By Location")
            FilterChip("By Date", selected = true)
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(5) {
                ShiftDay(day = "Mon", date = "26")
            }
        }
    }
}

@Composable
fun FilterChip(text: String, selected: Boolean = false) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = if (selected) MaterialTheme.colorScheme.primary else Color.LightGray),
    ) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text, color = if (selected) Color.White else Color.Black)
            if(!selected) Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }
    }
}


@Composable
fun ShiftDay(day: String, date: String, selected: Boolean = false) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (selected) MaterialTheme.colorScheme.primary else Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(day, style = MaterialTheme.typography.bodySmall)
            Text(date, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun TeamStatus() {
    Column {
        Text("Team Status", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        TeamMember(name = "Sarah Jones", role = "Cashier", status = "On Shift", avatar = R.drawable.ic_launcher_background)
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        TeamMember(name = "Mike Lee", role = "Barista", status = "Next at 4 PM", avatar = R.drawable.ic_launcher_background)
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        TeamMember(name = "Chloe Davis", role = "Shift Lead", status = "Off-Duty", avatar = R.drawable.ic_launcher_background)
    }
}

@Composable
fun TeamMember(name: String, role: String, status: String, avatar: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = avatar),
            contentDescription = name,
            modifier = Modifier.size(40.dp).clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.Bold)
            Text(role, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Box(
            modifier = Modifier.size(8.dp).background(
                color = when (status) {
                    "On Shift" -> Color.Green
                    "Off-Duty" -> Color.Gray
                    else -> Color.Yellow
                },
                shape = CircleShape
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(status, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun QuickActionsAndDistribution() {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
        Column {
            Text("Quick Actions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                QuickAction("New Shift", Icons.Default.Add)
                Spacer(modifier = Modifier.width(16.dp))
                QuickAction("Approvals", Icons.Default.CheckCircle)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                QuickAction("Announce", Icons.Default.Campaign)
                Spacer(modifier = Modifier.width(16.dp))
                QuickAction("Team", Icons.Default.Groups)
            }
        }
        Column {
            Text("Distribution", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            // Placeholder for Donut Chart
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp)) {
                Icon(Icons.Outlined.BarChart, contentDescription = "Distribution Chart", modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun QuickAction(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = text, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun Announcements() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Announcements", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("View All", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        AnnouncementCard(
            title = "All-staff meeting this Friday",
            body = "Reminder: All-staff meeting this Friday at 10 AM in the main conference room to discuss Q3 goals."
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnnouncementCard(
            title = "New Holiday Request Policy",
            body = "Please review the updated holiday request policy in the documents section. All requests must be submitted 2 weeks in advance."
        )
    }
}

@Composable
fun AnnouncementCard(title: String, body: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(body, style = MaterialTheme.typography.bodyMedium)
        }
    }
}


@Composable
fun ManagerDashboardBottomBar() {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Dashboard, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            selected = true,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.CalendarMonth, contentDescription = "Schedule") },
            label = { Text("Schedule") },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Mail, contentDescription = "Requests") },
            label = { Text("Requests") },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Campaign, contentDescription = "Messages") },
            label = { Text("Messages") },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.MoreVert, contentDescription = "More") },
            label = { Text("More") },
            selected = false,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
fun ManagerDashboardScreenPreview() {
    ManagerDashboardScreen()
}
