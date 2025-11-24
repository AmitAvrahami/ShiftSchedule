package com.example.smartschedule.core.domain.model.constraints.enums


/**
 * סוג החוק השבועי:
 * UNAVAILABLE – העובד לא זמין (RecurringConstraint)
 * PREFERRED   – העדפה (עדיף שלא/כן לשבץ)
 * MANDATORY   – שיבוץ קבוע (StandingAssignment)
 */
enum class RuleKind {
    UNAVAILABLE,
    PREFERRED,
    MANDATORY
}
