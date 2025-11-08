package com.example.smartschedule.data.mapper

import com.example.smartschedule.domain.models.shift.AfterNoonShiftStrategy
import com.example.smartschedule.domain.models.shift.MorningShiftStrategy
import com.example.smartschedule.domain.models.shift.NightShiftStrategy
import com.example.smartschedule.domain.models.shift.Shift
import java.time.LocalDateTime

object ShiftMapper {

    fun fromMap(data: Map<String, Any>): Shift {
        val strategy = when (val strategyType = data["strategyType"] as String) {
            "MORNING" -> MorningShiftStrategy()
            "AFTERNOON" -> AfterNoonShiftStrategy()
            "NIGHT" -> NightShiftStrategy()
            else -> throw IllegalArgumentException("Unknown strategy: $strategyType")
        }

        return Shift(
            id = (data["id"] as String),
            startDate = LocalDateTime.ofEpochSecond(
                (data["startDate"] as Number).toLong(), 0, java.time.ZoneOffset.UTC
            ),
            endDate = LocalDateTime.ofEpochSecond(
                (data["endDate"] as Number).toLong(), 0, java.time.ZoneOffset.UTC
            ),
            strategy = strategy
        )
    }

    fun toMap(shift: Shift): Map<String, Any> {
        return mapOf(
            "id" to shift.id,
            "startDate" to shift.startDate.toEpochSecond(java.time.ZoneOffset.UTC),
            "endDate" to shift.endDate.toEpochSecond(java.time.ZoneOffset.UTC),
            "strategyType" to shift.strategy.getShiftStrategyType().name
        )
    }
}
