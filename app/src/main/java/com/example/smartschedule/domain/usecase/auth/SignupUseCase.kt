package com.example.smartschedule.domain.usecase.auth

import com.example.smartschedule.data.repository.AuthRepository
import com.example.smartschedule.data.repository.Result
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(
        nationalId: String,
        name: String,
        email: String,
        password: String
    ): Flow<Result<FirebaseUser?>> {
        return authRepository.signup(
            nationalId = nationalId,
            name = name,
            email = email,
            password = password
        )
    }
}