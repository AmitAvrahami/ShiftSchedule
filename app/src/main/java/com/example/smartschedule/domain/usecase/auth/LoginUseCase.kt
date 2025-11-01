package com.example.smartschedule.domain.usecase.auth

import com.example.smartschedule.data.repository.auth.AuthRepository
import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.data.repository.user.UserRepository
import com.example.smartschedule.domain.models.user.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    operator fun invoke(
        email: String,
        password: String
    ): Flow<Result<User?>> = flow {

        emit(Result.Loading)

        when (val authResult = authRepository.login(email, password).last()) {

            is Result.Success -> {
                val userId = authResult.data?.uid
                    ?: return@flow emit(Result.Error(Exception("User id is null")))

                when (val firestoreResult = userRepository.getUserById(userId)) {
                    is Result.Success -> emit(Result.Success(firestoreResult.data))
                    is Result.Error -> emit(Result.Error(firestoreResult.exception))
                    is Result.Loading -> emit(Result.Loading)
                }
            }

            is Result.Error -> emit(Result.Error(authResult.exception))
            is Result.Loading -> emit(Result.Loading)
        }
    }
}