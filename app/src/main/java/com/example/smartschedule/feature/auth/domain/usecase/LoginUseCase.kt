package com.example.smartschedule.feature.auth.domain.usecase

import com.example.smartschedule.core.domain.utils.Result
import com.example.smartschedule.feature.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Flow<Result<FirebaseUser?>> {
        return authRepository.login(email, password)
    }
}