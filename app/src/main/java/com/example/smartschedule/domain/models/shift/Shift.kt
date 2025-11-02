package com.example.smartschedule.domain.models.shift

import com.example.smartschedule.domain.models.constraint.ConstraintStrategy
import java.time.LocalDateTime

data class Shift(
    val id: Long,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val strategy: ShiftStrategy,
 //   val assignedEmployees: List<Employee>
)

interface ShiftStrategy{
    fun getStartTime(): LocalDateTime
    fun getEndTime(): LocalDateTime
    fun getRequiredEmployees(): Int
    fun isOverNight(): Boolean

}

class MorningShiftStrategy : ShiftStrategy{
    override fun getStartTime(): LocalDateTime {
        TODO("Not yet implemented")
    }

    override fun getEndTime(): LocalDateTime {
        TODO("Not yet implemented")
    }

    override fun getRequiredEmployees(): Int {
        TODO("Not yet implemented")
    }

    override fun isOverNight(): Boolean {
        TODO("Not yet implemented")
    }

}

class AfterNoonShiftStrategy : ShiftStrategy{
    override fun getStartTime(): LocalDateTime {
        TODO("Not yet implemented")
    }

    override fun getEndTime(): LocalDateTime {
        TODO("Not yet implemented")
    }

    override fun getRequiredEmployees(): Int {
        TODO("Not yet implemented")
    }

    override fun isOverNight(): Boolean {
        TODO("Not yet implemented")
    }

}

class NightShiftStrategy: ShiftStrategy {
    override fun getStartTime(): LocalDateTime {
        TODO("Not yet implemented")
    }

    override fun getEndTime(): LocalDateTime {
        TODO("Not yet implemented")
    }

    override fun getRequiredEmployees(): Int {
        TODO("Not yet implemented")
    }

    override fun isOverNight(): Boolean {
        TODO("Not yet implemented")
    }
}

