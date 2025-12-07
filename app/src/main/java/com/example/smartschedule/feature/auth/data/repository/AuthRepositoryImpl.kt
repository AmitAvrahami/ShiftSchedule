package com.example.smartschedule.feature.auth.data.repository

import android.util.Log
import com.example.smartschedule.core.domain.utils.Result
import com.example.smartschedule.feature.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth : FirebaseAuth
) : AuthRepository {

    private val tag = this::class.java.simpleName

    override val currentUser : FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(
        email : String ,
        password : String
    ) : Flow<Result<FirebaseUser?>> = flow {
        Log.d(tag, "login: with email: $email")
        emit(Result.Loading)
        val authResult = firebaseAuth.signInWithEmailAndPassword(
            email ,
            password
        ).await()
        emit(Result.Success(authResult.user))
    }.catch { e ->
        Log.e(tag, "login: failed for email: $email", e)
        emit(Result.Error(e))
    }

    override suspend fun signup(
        email : String ,
        password : String
    ) : Flow<Result<FirebaseUser?>> = flow {
        Log.d(tag, "signup: with email: $email")
        emit(Result.Loading)
        val authResult = firebaseAuth.createUserWithEmailAndPassword(
            email ,
            password
        ).await()
        val user = authResult.user ?: throw Exception("User creation failed")
        emit(Result.Success(user))
    }.catch { e ->
        Log.e(tag, "signup: failed for email: $email", e)
        emit(Result.Error(e))
    }

    override suspend fun logout(): Flow<Result<Unit>> = flow {
        Log.d(tag, "logout: initiated")
        emit(Result.Loading)
        try {
            firebaseAuth.signOut()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            Log.e(tag, "logout: failed", e)
            emit(Result.Error(e))
        }
    }
}