package com.example.smartschedule.domain.usecase.auth

import com.example.smartschedule.data.repository.auth.AuthRepository
import com.example.smartschedule.data.repository.Result
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String) : Flow<Result<FirebaseUser?>>{
        return authRepository.login(email, password)

    }
}