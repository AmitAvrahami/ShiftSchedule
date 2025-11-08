package com.example.smartschedule.data.repository.constraint

import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.models.constraint.Constraint
import java.time.LocalDate

interface ConstraintRepository {

    suspend fun createConstraint(constraint: Constraint): Result<Unit>

    suspend fun getConstraintById(id: Long): Result<Constraint?>

    suspend fun getAllConstraints(): Result<List<Constraint>>

    suspend fun getConstraintsByEmployee(employeeId: String): Result<List<Constraint>>

    suspend fun getActiveConstraints(): Result<List<Constraint>>

    suspend fun getConstraintsByDateRange(startDate: LocalDate, endDate: LocalDate): Result<List<Constraint>>

    suspend fun updateConstraint(constraint: Constraint): Result<Unit>

    suspend fun setConstraintActive(id: Long, isActive: Boolean): Result<Unit>

    suspend fun deleteConstraint(id: Long): Result<Unit>
}
