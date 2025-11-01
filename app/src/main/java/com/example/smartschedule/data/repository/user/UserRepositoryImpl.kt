package com.example.smartschedule.data.repository.user

import com.example.smartschedule.domain.models.user.User
import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.models.user.roles.Role
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
) : UserRepository {
    private val usersCollection = fireStore.collection("users")

    override suspend fun createUser(user: User): Result<Unit> {
        return try {
            val roleMap = hashMapOf(
                "roleName" to user.role.getRole().displayName,
                "roleDescription" to user.role.getPermissions()
            )
            val userMap = hashMapOf(
                "uid" to user.id,
                "fullName" to user.fullName,
                "nationalId" to user.nationalId,
                "role" to roleMap,
                "isActive" to user.isActive
            )

            usersCollection
                .document(user.id)
                .set(userMap)
                .await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getUserById(uid: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: User): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(uid: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUsers(): Result<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUsersByRole(role: Role): Result<List<User>> {
        TODO("Not yet implemented")
    }
}