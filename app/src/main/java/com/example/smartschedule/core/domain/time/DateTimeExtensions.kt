package com.example.smartschedule.core.domain.time


fun Long.convertLongToIsoDateString(): String {
    return java.time.Instant.ofEpochMilli(this)
        .atZone(java.time.ZoneId.systemDefault())
        .toLocalDate()
        .toString()
}