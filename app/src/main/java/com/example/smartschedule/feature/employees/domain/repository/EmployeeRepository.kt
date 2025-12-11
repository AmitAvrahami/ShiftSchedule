package com.example.smartschedule.feature.employees.domain.repository

import com.example.smartschedule.core.domain.model.employees.Employee
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface EmployeeRepository{

    suspend fun getEmployeeById(employeeId: EmployeeId): Flow<Result<Employee>>

    suspend fun updateEmployeeById(employeeId : EmployeeId, updatedUser: Employee) : Flow<Result<Unit>>

    fun deleteEmployeeById(employeeId : EmployeeId): Flow<Result<Unit>>
}