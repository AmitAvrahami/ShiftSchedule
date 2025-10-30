package com.example.smartschedule.data.repository

import com.example.smartschedule.domain.models.user.User
import com.example.smartschedule.domain.models.user.roles.EmployeeRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Flow<Result<FirebaseUser?>> = flow {
        emit(Result.Loading)
        val user = firebaseAuth.signInWithEmailAndPassword(email, password).await().user
        emit(Result.Success(user))
    }.catch { e ->
        emit(Result.Error(e))
    }

    override suspend fun signup(
        nationalId: String,
        name: String,
        email: String,
        password: String,
    ): Flow<Result<FirebaseUser?>> = flow {
        emit(Result.Loading)
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = authResult.user ?: throw Exception("User creation failed")

        val role = EmployeeRole(hireDate = LocalDate.now())

        val userDetails = User(
            id = user.uid,
            fullName = name,
            nationalId = nationalId,
            role = role,
            isActive = true
        )

        fireStore.collection("users")
            .document(user.uid)
            .set(
                mapOf(
                    "uid" to user.uid,
                    "fullName" to name,
                    "nationalId" to nationalId,
                    "email" to email,
                    "role" to "employee",
                    "hireDate" to LocalDate.now().toString(),
                    "isActive" to true,
                    "createdAt" to System.currentTimeMillis()
                )
            )
            .await()

        emit(Result.Success(user))
    }.catch { e ->
        emit(Result.Error(e))
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }
}
