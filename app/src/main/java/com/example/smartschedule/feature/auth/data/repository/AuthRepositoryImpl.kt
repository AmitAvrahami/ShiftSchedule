package com.example.smartschedule.feature.auth.data.repository

import android.util.Log
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.utils.Result
import com.example.smartschedule.feature.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth : FirebaseAuth
) : AuthRepository {

    private val tag = this::class.java.simpleName
    private var userId : EmployeeId? = null
    override fun getCurrentUserId() : EmployeeId? {
        return userId
    }

    override suspend fun login(
        email : String ,
        password : String
    ) : Flow<Result<EmployeeId>> = flow {
        Log.d(tag, "login: with email: $email")
        emit(Result.Loading)
        val authResult = firebaseAuth.signInWithEmailAndPassword(
            email ,
            password
        ).await()
        val uid = authResult.user?.uid ?: throw Exception("User ID is null")
        userId = EmployeeId(uid)
        Log.d(tag, "login: success for email: $email")
        emit(Result.Success(EmployeeId(uid)))
    }.catch { e ->
        Log.e(tag, "login: failed for email: $email", e)
        userId = null
        emit(Result.Error(e))
    }

    override suspend fun signup(
        email : String ,
        password : String
    ) : Flow<Result<EmployeeId>> = flow {
        Log.d(tag, "signup: with email: $email")
        emit(Result.Loading)
        val authResult = firebaseAuth.createUserWithEmailAndPassword(
            email ,
            password
        ).await()
        val uid = authResult.user?.uid ?: throw Exception("User creation failed")
        Log.d(tag, "signup: success for email: $email")
        userId = EmployeeId(uid)
        emit(Result.Success(EmployeeId(uid)))
    }.catch { e ->
        Log.e(tag, "signup: failed for email: $email", e)
        userId = null
        emit(Result.Error(e))
    }

    override suspend fun logout(): Flow<Result<Unit>> = flow {
        Log.d(tag, "logout: initiated")
        emit(Result.Loading)
        try {
            firebaseAuth.signOut()
            userId = null
            Log.d(tag, "logout: success")
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            Log.e(tag, "logout: failed", e)
            emit(Result.Error(e))
        }
    }
}