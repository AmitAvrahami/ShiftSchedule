package com.example.smartschedule.core.data.firebase.model

import com.example.smartschedule.core.data.mapper.tryParse
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.WorkSchedule
import com.example.smartschedule.core.domain.model.smartSchedule.WorkScheduleId
import com.example.smartschedule.core.domain.model.smartSchedule.enums.BoardStatus
import java.time.LocalDate
import java.time.LocalDateTime

data class ScheduleDto(
    val scheduleId: String = "",
    val name: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val status: String = BoardStatus.DRAFT.name,
    val notes: String? = null,
    val shifts: List<ShiftDto> = emptyList(),
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updateDate: Long? = null
)



