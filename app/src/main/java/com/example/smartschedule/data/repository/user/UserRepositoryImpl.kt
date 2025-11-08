package com.example.smartschedule.data.repository.user

import com.example.smartschedule.data.mapper.RoleMapper
import com.example.smartschedule.data.mapper.RoleMapper.asMapOrEmpty
import com.example.smartschedule.domain.models.user.User
import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.models.user.roles.Role
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    fireStore: FirebaseFirestore,
) : UserRepository {
    private val usersCollection = fireStore.collection("users")

    override suspend fun createUser(user: User): Result<Unit> {
        return try {
            val userMap = hashMapOf(
                "uid" to user.id,
                "fullName" to user.fullName,
                "nationalId" to user.nationalId,
                "role" to RoleMapper.toMap(user.role),
                "isActive" to user.isActive
            )
            usersCollection.document(user.id).set(userMap).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getUserById(uid: String): Result<User> {
        return try {
            val doc = usersCollection.document(uid).get().await()
            if (!doc.exists()) return Result.Error(Exception("User not found"))

            val data = doc.data ?: return Result.Error(Exception("User data is empty"))
            val roleMap = data["role"].asMapOrEmpty<String, Any>()
            val role = RoleMapper.fromMap(roleMap)

            val user = User(
                id = data["uid"] as String,
                fullName = data["fullName"] as String,
                nationalId = data["nationalId"] as String,
                role = role,
                isActive = data["isActive"] as Boolean
            )

            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
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