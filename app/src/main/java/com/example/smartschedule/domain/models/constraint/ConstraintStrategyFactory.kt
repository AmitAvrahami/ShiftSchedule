package com.example.smartschedule.domain.models.constraint

object ConstraintStrategyFactory {
    fun create(type: String): ConstraintStrategy {
        return when (type.uppercase()) {
            "FULLDAYCONSTRAINTSTRATEGY" -> FullDayConstraintStrategy()
            "SHIFTCONSTRAINTSTRATEGY" -> ShiftConstraintStrategy()
            "DATERANGECONSTRAINTSTRATEGY" -> DateRangeConstraintStrategy()
            "RECURRINGCONSTRAINTSTRATEGY" -> RecurringConstraintStrategy()
            else -> FullDayConstraintStrategy()
        }
    }
}
