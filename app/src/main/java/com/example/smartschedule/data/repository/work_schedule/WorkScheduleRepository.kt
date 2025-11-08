package com.example.smartschedule.domain.repository.workschedule

import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.models.workschedule.WorkSchedule
import com.example.smartschedule.domain.models.workschedule.ScheduleStatus
import java.time.LocalDate

interface WorkScheduleRepository {

    suspend fun createWorkSchedule(schedule: WorkSchedule): Result<Unit>

    suspend fun getWorkScheduleById(id: Long): Result<WorkSchedule?>

    suspend fun getAllWorkSchedules(
        status: ScheduleStatus? = null
    ): Result<List<WorkSchedule>>

    suspend fun getWorkSchedulesByDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<List<WorkSchedule>>

    suspend fun updateWorkSchedule(schedule: WorkSchedule): Result<Unit>

    suspend fun updateScheduleStatus(
        scheduleId: Long,
        newStatus: ScheduleStatus
    ): Result<Unit>

    suspend fun approveWorkSchedule(
        scheduleId: Long,
        managerId: String
    ): Result<Unit>

    suspend fun deleteWorkSchedule(scheduleId: Long): Result<Unit>

    suspend fun getActiveWorkSchedule(): Result<WorkSchedule?>

    suspend fun existsInRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<Boolean>
}
