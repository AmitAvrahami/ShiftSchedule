package com.example.smartschedule.domain.usecase.auth

import com.example.smartschedule.data.repository.auth.AuthRepository
import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.data.repository.user.UserRepository
import com.example.smartschedule.domain.models.user.User
import com.example.smartschedule.domain.models.user.roles.AdminRole
import com.example.smartschedule.domain.models.user.roles.EmployeeRole
import com.example.smartschedule.domain.models.user.roles.ManagerRole
import com.example.smartschedule.domain.models.user.roles.Role
import com.example.smartschedule.domain.models.user.roles.Roles
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import java.time.LocalDate
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        role: Roles,
        nationalId: String,
        name: String,
        email: String,
        password: String,
    ): Flow<Result<FirebaseUser?>> = flow {

        emit(Result.Loading)

        // 🔹 שלב 1: הרשמה ב־FirebaseAuth
        val authResult = authRepository.signup(email, password).last() // ✅ מחכה לתוצאה הסופית

        when (authResult) {
            is Result.Success -> {
                val firebaseUser = authResult.data
                    ?: return@flow emit(Result.Error(Exception("Firebase user is null")))

                // 🔹 שלב 2: יצירת אובייקט User מלא
                val newUser = User(
                    id = firebaseUser.uid,
                    nationalId = nationalId,
                    fullName = name,
                    role = createRole(role),
                    isActive = true
                )

                // 🔹 שלב 3: שמירה ב־Firestore
                when (val firestoreResult = userRepository.createUser(newUser)) {
                    is Result.Success -> emit(Result.Success(firebaseUser))
                    is Result.Error -> emit(Result.Error(firestoreResult.exception))
                    is Result.Loading -> emit(Result.Loading)
                }
            }

            is Result.Error -> emit(Result.Error(authResult.exception))
            is Result.Loading -> emit(Result.Loading)
        }
    }

    private fun createRole(role: Roles): Role {
        return when (role) {
            Roles.EMPLOYEE -> EmployeeRole(hireDate = LocalDate.now())
            Roles.MANAGER -> ManagerRole(hireDate = LocalDate.now())
            Roles.ADMIN -> AdminRole()
        }
    }
}
