package com.example.smartschedule.feature.auth.domain.repository

import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.utils.Result
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun getCurrentUserId() : EmployeeId?

    suspend fun login(email: String, password: String): Flow<Result<EmployeeId>>
    suspend fun signup(email: String, password: String): Flow<Result<EmployeeId>>
    suspend fun logout(): Flow<Result<Unit>>
}