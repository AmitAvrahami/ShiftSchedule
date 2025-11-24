package com.example.smartschedule.core.domain.model.smartSchedule.enums

enum class BoardStatus {
    DRAFT,            // בטיוטה
    PENDING_APPROVAL, // נשלח לאישור
    APPROVED,         // אושר ע"י מנהלת
    PUBLISHED,        // פורסם לעובדים
    LOCKED,           // נעול אחרי פרסום
    ARCHIVED          // הועבר לארכיון
}
