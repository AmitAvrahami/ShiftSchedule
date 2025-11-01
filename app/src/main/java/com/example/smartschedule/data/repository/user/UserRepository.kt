package com.example.smartschedule.data.repository.user

import com.example.smartschedule.domain.models.user.User
import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.models.user.roles.Role

interface UserRepository {

    suspend fun createUser(user: User): Result<Unit>

    suspend fun getUserById(uid: String): Result<User>

    suspend fun updateUser(user: User): Result<Unit>

    suspend fun deleteUser(uid: String): Result<Unit>

    suspend fun getAllUsers(): Result<List<User>>

    suspend fun getUsersByRole(role: Role): Result<List<User>>
}
