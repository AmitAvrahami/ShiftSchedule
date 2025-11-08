package com.example.smartschedule.data.repository.shift_assigment

import com.example.smartschedule.domain.models.shiftassignment.Assignment
import com.example.smartschedule.data.repository.Result


interface ShiftAssignmentRepository {
    suspend fun getNextAssignmentByUserId(userId: String): Result<Assignment?>

}