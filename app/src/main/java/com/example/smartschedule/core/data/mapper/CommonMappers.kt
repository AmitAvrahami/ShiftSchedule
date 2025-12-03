package com.example.smartschedule.core.data.mapper

inline fun <T> String.tryParse(
    parse: (String) -> T,
    defaultValue: () -> T
) : T {
    return try {
        parse(this)
    } catch (e: IllegalArgumentException) {
        defaultValue()
    }
}