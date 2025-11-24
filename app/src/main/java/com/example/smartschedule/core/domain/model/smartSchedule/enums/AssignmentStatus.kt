package com.example.smartschedule.core.domain.model.smartSchedule.enums

enum class AssignmentStatus {
    ACTIVE,          // שיבוץ פעיל
    PENDING_SWAP,    // בקשת החלפה ממתינה לאישור
    SWAPPED,         // השיבוץ הוחלף עם עובד אחר
    EMERGENCY_FILL,  // אויש בקריאת חירום
    CANCELED         // השיבוץ בוטל לגמרי
}
