package com.example.smartschedule.core.data.firebase.model

import com.example.smartschedule.core.domain.model.employees.Employee
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.employees.enums.EmployeeRole
import com.example.smartschedule.core.domain.model.employees.enums.EmploymentType

data class UserDto(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val avatar: String? = null,
    val nationalId: String? = null,
    val phoneNumber: String? = null,
    val role: String = "WORKER",
    val isActive: Boolean = true,
    val colorHex: String = "#808080",
    val employmentType: String = "FULL_TIME",
    val preferredShiftTypeIds: List<Int> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

