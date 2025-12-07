package com.example.smartschedule.feature.auth.domain.usecase

import android.util.Log
import com.example.smartschedule.core.domain.utils.Result
import com.example.smartschedule.feature.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    private val tag = this::class.java.simpleName
    suspend operator fun invoke(email: String, password: String): Flow<Result<FirebaseUser?>> {
        Log.d(tag, "invoke: Login with email: $email")
        return authRepository.login(email, password)
    }
}