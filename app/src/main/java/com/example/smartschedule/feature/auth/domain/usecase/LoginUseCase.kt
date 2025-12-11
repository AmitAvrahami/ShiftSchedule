package com.example.smartschedule.feature.auth.domain.usecase

import android.util.Log
import com.example.smartschedule.core.data.datastore.UserPreferences
import com.example.smartschedule.core.domain.model.employees.Employee
import com.example.smartschedule.core.domain.utils.Result
import com.example.smartschedule.feature.auth.domain.repository.AuthRepository
import com.example.smartschedule.feature.employees.domain.repository.EmployeeRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val employeeRepository: EmployeeRepository,
    private val userPreferences: UserPreferences
) {
    private val tag = this::class.java.simpleName
    suspend operator fun invoke(email: String, password: String): Flow<Result<Employee>> = flow {
        emit(Result.Loading)
        Log.d(tag, "Starting login for email: $email")
        authRepository.login(
            email ,
            password
        ).collect { authResult ->
            when (authResult) {
                is Result.Success -> {
                    val employeeId = authResult.data
                    Log.d(tag, "Authentication successful, employeeId: $employeeId")
                    employeeRepository.getEmployeeById(employeeId).collect { employeeResult ->
                        when (employeeResult) {
                            is Result.Success -> {
                                val employee = employeeResult.data
                                Log.d(tag, "Successfully fetched employee profile for: ${employee.fullName}")
                                userPreferences.saveUser(
                                    role = employee.role ,
                                    userId = employeeId
                                )
                                Log.d(
                                    tag ,
                                    "Login flow complete. User: ${employee.fullName}"
                                )
                                emit(Result.Success(employee))
                            }

                            is Result.Error -> {
                                Log.e(
                                    tag ,
                                    "Failed to fetch user profile" ,
                                    employeeResult.exception
                                )
                                emit(Result.Error(employeeResult.exception))
                            }

                            is Result.Loading -> {
                                Log.d(tag, "Fetching employee profile...")
                                emit(Result.Loading)
                            }
                        }
                    }
                }

                is Result.Error -> {
                    Log.e(
                        tag ,
                        "Auth failed" ,
                        authResult.exception
                    )
                    emit(Result.Error(authResult.exception))
                }

                is Result.Loading -> {
                    Log.d(tag, "Authentication in progress...")
                    emit(Result.Loading)
                }
            }
        }
    }
}