package com.example.smartschedule.data.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import com.example.smartschedule.data.repository.Result

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Flow<Result<FirebaseUser?>>
    suspend fun signup(nationalId: String,name: String, email: String, password: String): Flow<Result<FirebaseUser?>>

    suspend fun logout()
}
