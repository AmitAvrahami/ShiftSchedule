package com.example.smartschedule.core.domain.model.smartSchedule

import com.example.smartschedule.core.domain.model.smartSchedule.enums.BoardStatus
import java.time.LocalDateTime


data class ScheduleBoard(
    val id: Int,
    val workScheduleId: Int,
    val approvedBy: Int?,
    val creationDate: LocalDateTime ,
    val updateDate: LocalDateTime?,
    val notesDescription: String?,
    val status: BoardStatus
)//Todo : Refactor the Ids
