package com.example.smartschedule.feature.employees.data.repository

import android.util.Log
import com.example.smartschedule.core.data.firebase.constants.Collections
import com.example.smartschedule.core.data.firebase.model.UserDto
import com.example.smartschedule.core.data.mapper.toDomain
import com.example.smartschedule.core.data.mapper.toDto
import com.example.smartschedule.core.domain.model.employees.Employee
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.feature.employees.domain.repository.EmployeeRepository
import com.example.smartschedule.core.domain.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class EmployeeRepositoryImpl @Inject constructor(
    private val fireStore : FirebaseFirestore
): EmployeeRepository {

    private val tag = this::class.java.simpleName
    private val usersCollection = fireStore.collection(Collections.USERS.path)


    override suspend fun getEmployeeById(employeeId : EmployeeId) : Flow<Result<Employee>> = flow {
        emit(Result.Loading)
        try {
            val snapshot = usersCollection.document(employeeId.value).get().await()
            if (snapshot.exists()) {
                val userDto = snapshot.toObject(UserDto::class.java)
                if (userDto != null) {
                    emit(Result.Success(userDto.toDomain()))
                } else {
                    emit(Result.Error(Exception("Failed to map document to UserDto")))
                }
            } else {
                emit(Result.Error(Exception("User not found with ID: ${employeeId.value}")))
            }
        }catch (e: Exception) {
            Log.e(tag, "getUserById error", e)
            emit(Result.Error(e))
        }
    }

    override suspend fun updateEmployeeById(
        employeeId : EmployeeId ,
        updatedUser : Employee
    ) : Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val userDto = updatedUser.toDto()

            usersCollection.document(employeeId.value)
                .set(userDto, SetOptions.merge())
                .await()

            emit(Result.Success(Unit))
        } catch (e: Exception) {
            Log.e(tag, "updateUserById error", e)
            emit(Result.Error(e))
        }
    }

    override fun deleteEmployeeById(employeeId : EmployeeId) : Flow<Result<Unit>>  = flow {
        emit(Result.Loading)
        try {
            usersCollection.document(employeeId.value).delete().await()
            emit(Result.Success(Unit))
        }catch (e: Exception) {
            Log.e(tag, "deleteUserById error", e)
            emit(Result.Error(e))
        }
    }
}