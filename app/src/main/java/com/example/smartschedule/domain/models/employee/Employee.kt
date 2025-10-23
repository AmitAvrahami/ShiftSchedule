package com.example.smartschedule.domain.models.employee

import com.example.smartschedule.domain.models.constraint.Constraint
import java.time.LocalDate

data class Employee(
    val id: Long,
    val fullName: String,
    val role : Roles,
    val nationalId : Long,
    val isActive : Boolean,
    val constraints : List<Constraint> = emptyList(),
    val maxShiftsPerWeek : Int = 5,
    val minShiftsPerWeek : Int = 3,
    val hireDate : LocalDate? = null,
    val notes : String? = null,
    val photo : String? = null

)

enum class Roles{
    MANAGER,
    EMPLOYEE
}