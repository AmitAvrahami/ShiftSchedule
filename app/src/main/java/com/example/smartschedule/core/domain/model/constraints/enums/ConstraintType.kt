package com.example.smartschedule.core.domain.model.constraints.enums


/**
 * סוג האילוץ:
 * FULL_DAY   – כל היום חסום
 * PARTIAL_DAY – חלק מהיום (שעות מסוימות)
 * DATE_RANGE – טווח תאריכים
 * WEEKLY_RECURRING – אילוץ שבועי קבוע
 */
enum class ConstraintType {
    FULL_DAY,
    PARTIAL_DAY,
    DATE_RANGE,
    WEEKLY_RECURRING
}
