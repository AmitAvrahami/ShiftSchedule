package com.example.smartschedule.domain.models.shift

object ShiftStrategyFactory {
    fun create(type: ShiftStrategyType): ShiftStrategy {
        return when (type) {
            ShiftStrategyType.MORNING -> MorningShiftStrategy()
            ShiftStrategyType.AFTERNOON -> AfterNoonShiftStrategy()
            ShiftStrategyType.NIGHT -> NightShiftStrategy()
        }
    }

    fun fromString(type: String): ShiftStrategy {
        return create(ShiftStrategyType.valueOf(type.uppercase()))
    }
}
