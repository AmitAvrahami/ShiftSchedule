package com.example.smartschedule.feature.auth.domain.usecase

import com.example.smartschedule.core.domain.utils.Result
import com.example.smartschedule.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Flow<Result<Unit>> {
        return authRepository.logout()
    }
}