package com.example.smartschedule.feature.smartSchedule.domain.repository

import com.example.smartschedule.core.domain.model.constraints.Constraint
import com.example.smartschedule.core.domain.model.constraints.WeeklyRule
import com.example.smartschedule.core.domain.model.employees.Employee
import com.example.smartschedule.core.domain.model.smartSchedule.WorkSchedule
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    fun getWorkSchedule(scheduleId: String): Flow<WorkSchedule>
    fun getEmployees(): Flow<List<Employee>>

    fun getConstraints(start: Long, end: Long): Flow<List<Constraint>>

    fun getWeeklyRules(): Flow<List<WeeklyRule>>

    suspend fun updateSchedule(schedule: WorkSchedule): Result<Unit>
}