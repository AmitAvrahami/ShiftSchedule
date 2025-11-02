package com.example.smartschedule.domain.usecase.user

import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.data.repository.user.UserRepository
import com.example.smartschedule.domain.models.user.User
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String): Result<User?> {
        return when (val result = repository.getUserById(userId)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.exception)
            Result.Loading -> Result.Loading
        }
    }

}
