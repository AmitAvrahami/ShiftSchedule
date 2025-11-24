package com.example.smartschedule.core.domain.model.smartSchedule

import com.example.smartschedule.core.domain.model.smartSchedule.enums.ShiftType
import java.time.LocalDate
import java.time.LocalTime

@JvmInline
value class ShiftId(val value: Int)

data class Shift(
    val id: ShiftId,
    val date: LocalDate ,
    val shiftType: ShiftType ,
    val startTime: LocalTime ,
    val endTime: LocalTime,
    val notes: String?
){
    companion object {
        fun fromType(
            id: ShiftId,
            date: LocalDate,
            shiftType: ShiftType,
            notes: String? = null
        ): Shift = Shift(
            id = id,
            date = date,
            shiftType = shiftType,
            startTime = shiftType.defaultStartTime,
            endTime = shiftType.defaultEndTime,
            notes = notes
        )
    }
}
