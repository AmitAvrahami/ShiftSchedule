package com.example.smartschedule.feature.auth.domain.usecase

import android.util.Log
import com.example.smartschedule.core.domain.utils.Result
import com.example.smartschedule.core.data.datastore.UserPreferences
import com.example.smartschedule.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository ,
    private val sessionManager: UserPreferences
) {
    private val tag = this::class.java.simpleName
    suspend operator fun invoke(): Flow<Result<Unit>> = flow {
        Log.d(tag, "invoke: Logout initiated")

        emit(Result.Loading)

        authRepository.logout().collect { authResult ->
            when (authResult) {

                is Result.Success -> {
                    Log.d(tag, "invoke: Logout successful, clearing session")
                    sessionManager.clear()
                    emit(Result.Success(Unit))
                }

                is Result.Error -> {
                    Log.e(tag, "invoke: Logout failed", authResult.exception)
                    emit(Result.Error(authResult.exception))
                }
                is Result.Loading -> emit(Result.Loading)
            }
        }
    }
}