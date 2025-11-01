package com.example.smartschedule.data.repository.auth

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import com.example.smartschedule.data.repository.Result

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Flow<Result<FirebaseUser?>>
    suspend fun signup(email: String, password: String): Flow<Result<FirebaseUser?>>

    suspend fun logout()

}
