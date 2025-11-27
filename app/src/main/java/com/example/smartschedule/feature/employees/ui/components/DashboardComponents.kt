package com.example.smartschedule.feature.employees.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartschedule.core.domain.model.smartSchedule.Shift
import com.example.smartschedule.feature.employees.ui.ShiftDisplayModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// 1. כרטיס ה-Hero הכחול (המשמרת הבאה)
@Composable
fun NextShiftCard(
    shift: Shift?,
    hoursUntil: Long?,
    onDetailsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF42A5F5), Color(0xFF1976D2))
                    )
                )
                .padding(20.dp)
        ) {
            if (shift != null) {
                Column(modifier = Modifier.align(Alignment.TopStart)) {
                    Text(
                        text = "המשמרת הבאה בעוד ${hoursUntil ?: "-"} שעות",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${shift.startTime} - ${shift.endTime}",
                        style = MaterialTheme.typography.headlineLarge, // מספרים גדולים
                        color = Color.White
                    )
                    Text(
                        text = "${shift.shiftType.displayName} | סניף ראשי",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Button(
                    onClick = onDetailsClick,
                    modifier = Modifier.align(Alignment.BottomEnd),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("פרטים", color = Color(0xFF1976D2), fontWeight = FontWeight.Bold)
                }
            } else {
                Text(
                    text = "אין משמרות קרובות 🎉",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}

// 2. שורת "משמרות להיום" (עם תמונות עובדים/חנות)
@Composable
fun TodayShiftCard(
    model: ShiftDisplayModel
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(140.dp)
            .padding(end = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // כאן אפשר לשים תמונה של החנות או אווטארים
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray) // Placeholder לתמונה
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${model.shift.startTime} - ${model.shift.endTime}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            // רשימת עובדים
            val workersText = if (model.workerNames.isNotEmpty())
                model.workerNames.joinToString(", ")
            else "אין עובדים"

            Text(
                text = workersText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}

// 3. פעולות מהירות (העיגולים למטה)
@Composable
fun QuickActionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = Color(0xFFE3F2FD), // תכלת בהיר
            modifier = Modifier.size(64.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF1976D2),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
    }
}

// 4. הודעות מנהל
@Composable
fun AnnouncementCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = null,
                tint = Color.Red
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = "הודעות מנהל", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "לוח זמנים חדש לחגים\nאנא עברו על הלוח המעודכן...", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "פורסם ב-13 ביולי", style = MaterialTheme.typography.labelSmall, color = Color.LightGray)
            }
        }
    }
}