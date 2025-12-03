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

fun ConstraintDto.toDomain(): Constraint {
    return Constraint(
        id = ConstraintId(constraintId),
        employeeId = EmployeeId(employeeId),

        startDate = startDate.tryParse({ LocalDate.parse(it) }, { LocalDate.now()}),

        endDate = endDate.tryParse({ LocalDate.parse(it) }, { LocalDate.now()}),


        startTime = startTime?.let {it.tryParse({ LocalTime.parse(it) }, { LocalTime.now()})},

        endTime = endTime?.let {it.tryParse({ LocalTime.parse(it) }, { LocalTime.now()})},

        type = type.tryParse({ ConstraintType.valueOf(it)},
            { ConstraintType.FULL_DAY }),

        reason = reason
    )


}