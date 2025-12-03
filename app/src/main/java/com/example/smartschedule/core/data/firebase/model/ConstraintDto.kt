package com.example.smartschedule.core.data.firebase.model

import com.example.smartschedule.core.data.mapper.tryParse
import com.example.smartschedule.core.domain.model.constraints.Constraint
import com.example.smartschedule.core.domain.model.constraints.ConstraintId
import com.example.smartschedule.core.domain.model.constraints.enums.ConstraintType
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import java.time.LocalDate
import java.time.LocalTime

data class ConstraintDto(
    val constraintId : String = "",
    val employeeId : String = "",
    val employeeName : String = "",
    val startDate: String = "",
    val endDate: String = "",
    val startTime: String? = null,
    val endTime: String? = null,
    val type : String = ConstraintType.FULL_DAY.name,
    val reason : String? = null,
    val createdAt : Long = System.currentTimeMillis(),
    val updatedAt : Long = System.currentTimeMillis()
)

