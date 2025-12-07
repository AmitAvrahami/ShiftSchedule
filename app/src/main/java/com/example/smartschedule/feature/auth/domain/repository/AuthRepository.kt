package com.example.smartschedule.feature.auth.domain.repository

import com.example.smartschedule.core.domain.utils.Result
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Flow<Result<FirebaseUser?>>
    suspend fun signup(email: String, password: String): Flow<Result<FirebaseUser?>>
    suspend fun logout(): Flow<Result<Unit>>
}