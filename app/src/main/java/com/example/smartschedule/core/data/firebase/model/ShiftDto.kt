package com.example.smartschedule.core.data.firebase.model

import com.example.smartschedule.core.data.mapper.tryParse
import com.example.smartschedule.core.domain.model.smartSchedule.Shift
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftId
import com.example.smartschedule.core.domain.model.smartSchedule.enums.ShiftType
import java.time.LocalDate
import java.time.LocalTime

data class ShiftDto(
    val shiftId: String = "",
    val date: String = "",
    val startTime : String = "",
    val endTime : String = "",
    val dayOfWeek : String = "",
    val type: String = ShiftType.MORNING.name,
    val assignments: List<AssignmentDto> = emptyList(),
    val requiredEmployees: Int = 2,
    val notes: String? = null,
//    val definitionId: String = ""
)


fun ShiftDto.toDomainShift(): Shift {
    return Shift(
        id = ShiftId(shiftId) ,
        date = date.tryParse(
            { LocalDate.parse(date) } ,
            { LocalDate.now() }) ,
        startTime = startTime.tryParse(
            { LocalTime.parse(startTime) } ,
            { LocalTime.MIN }) ,
        endTime = endTime.tryParse(
            { LocalTime.parse(endTime) } ,
            { LocalTime.MAX }) ,
        shiftType = type.tryParse(
            { ShiftType.valueOf(it) } ,
            { ShiftType.MORNING }) ,
        notes = notes ,
//        requiredEmployees = requiredEmployees,
//        definitionId = definitionId
    )
}