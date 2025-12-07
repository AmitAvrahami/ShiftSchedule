package com.example.smartschedule.core.data.firebase.constants

sealed class Collections(val path: String) {
    object USERS : Collections("users")
    object WORK_SCHEDULES : Collections("work_schedules")
    object SHIFTS : Collections("shifts")
    object ASSIGNMENTS : Collections("assignments")
    object CONSTRAINTS : Collections("constraints")
    object WEEKLY_RULES : Collections("weekly_rules")

}