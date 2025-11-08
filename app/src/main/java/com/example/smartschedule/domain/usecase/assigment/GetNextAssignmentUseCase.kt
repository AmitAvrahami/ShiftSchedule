package com.example.smartschedule.domain.usecase.assigment

import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.data.repository.shift_assigment.ShiftAssignmentRepository
import com.example.smartschedule.domain.models.shiftassignment.Assignment
import javax.inject.Inject

class GetNextAssignmentUseCase @Inject constructor(
    private val repository: ShiftAssignmentRepository
) {
    suspend operator fun invoke(userId: String): Result<Assignment?> {
        return repository.getNextAssignmentByUserId(userId)
    }
}
