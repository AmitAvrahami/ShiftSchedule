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

fun ScheduleDto.toDomain(): WorkSchedule {
    val start = startDate.tryParse( { LocalDate.parse(it) }, { LocalDate.now() })
    val end = endDate.tryParse( { LocalDate.parse(it) } ,{ LocalDate.now() })

    val domainShifts = shifts.map { it.toDomainShift() }


    val domainAssignments = shifts.flatMap { shiftDto ->
        shiftDto.assignments.map { assignmentDto ->
            assignmentDto.toDomainAssignment(shiftDto.shiftId, scheduleId)
        }
    }

    return WorkSchedule(
        id = WorkScheduleId(scheduleId),
        name = name,
        period = start..end,
        status = status.tryParse( { BoardStatus.valueOf(it) },{ BoardStatus.DRAFT }),
        notes = notes,
        shifts = domainShifts,
        assignments = domainAssignments,
        createdBy = EmployeeId(createdBy),
        creationDate = LocalDateTime.now(), // או המרה מ-Long
        updateDate = updateDate?.let { LocalDateTime.now() } // פשטות כרגע
    )
}
