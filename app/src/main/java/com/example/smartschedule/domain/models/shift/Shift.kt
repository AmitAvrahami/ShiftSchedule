package com.example.smartschedule.domain.models.shift

import com.example.smartschedule.domain.models.constraint.ConstraintStrategy
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

typealias Days = Long
typealias Hours = Long
typealias Minutes = Long

data class Shift(
    val id: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val strategy: ShiftStrategy,
 //   val assignedEmployees: List<Employee>
){
    fun getDurationFrom(dateTime : LocalDateTime) : Triple<Days, Hours, Minutes>{
        val duration = java.time.Duration.between(dateTime, this.startDate)
        val days = duration.toDays()
        val hours = (duration.toHours() % 24)
        val minutes = (duration.toMinutes() % 60)
        return Triple(days, hours, minutes)
    }
}

interface ShiftStrategy{

    fun getShiftStrategyType(): ShiftStrategyType
    fun getStartTime(): LocalDateTime
    fun getEndTime(): LocalDateTime
    fun getRequiredEmployees(): Int
    fun isOverNight(): Boolean

}

class MorningShiftStrategy : ShiftStrategy {

    override fun getShiftStrategyType() = ShiftStrategyType.MORNING

    override fun getStartTime(): LocalDateTime =
        LocalDateTime.of(LocalDate.now(), LocalTime.of(6, 45))

    override fun getEndTime(): LocalDateTime =
        LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 45))

    override fun getRequiredEmployees(): Int = 3

    override fun isOverNight(): Boolean = false
}

class AfterNoonShiftStrategy : ShiftStrategy {

    override fun getShiftStrategyType() = ShiftStrategyType.AFTERNOON

    override fun getStartTime(): LocalDateTime =
        LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 45))

    override fun getEndTime(): LocalDateTime =
        LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 45))

    override fun getRequiredEmployees(): Int = 3

    override fun isOverNight(): Boolean = false
}

class NightShiftStrategy : ShiftStrategy {

    override fun getShiftStrategyType() = ShiftStrategyType.NIGHT

    override fun getStartTime(): LocalDateTime =
        LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 45))

    override fun getEndTime(): LocalDateTime =
        LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(6, 45))

    override fun getRequiredEmployees(): Int = 2

    override fun isOverNight(): Boolean = true
}

enum class ShiftStrategyType(val displayName: String){
    MORNING("Morning"), AFTERNOON("Afternoon"), NIGHT("Night")
}

