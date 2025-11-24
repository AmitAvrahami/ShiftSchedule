package com.example.smartschedule.core.domain.model.employees

import com.example.smartschedule.core.domain.model.employees.enums.EmployeeRole
import com.example.smartschedule.core.domain.model.employees.enums.EmploymentType

@JvmInline
value class EmployeeId(val value: Int)

data class Employee(
    val id: EmployeeId,
    val fullName: String,
    val role: EmployeeRole ,
    val isActive: Boolean,
    val preferredShiftTypeIds: List<Int>,
    val employmentType: EmploymentType
)

