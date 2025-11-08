package com.example.smartschedule.domain.repository.shift

import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.models.shift.Shift
import java.time.LocalDate

interface ShiftRepository {

    suspend fun createShift(shift: Shift): Result<Unit>

    suspend fun getShiftById(id: Long): Result<Shift?>

    suspend fun getAllShifts(): Result<List<Shift>>

    suspend fun getShiftsByType(type: String): Result<List<Shift>>

    suspend fun getShiftsByDate(date: LocalDate): Result<List<Shift>>

    suspend fun updateShift(shift: Shift): Result<Unit>

    suspend fun deleteShift(id: Long): Result<Unit>
}
