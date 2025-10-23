package com.example.smartschedule.domain.models.constraint

import androidx.compose.animation.core.AnimationEndReason
import com.example.smartschedule.domain.models.employee.Employee
import com.example.smartschedule.domain.models.shift.Shift

data class Constraint(
    val id : Long,
    val employee: Employee,
    val reason: String? = null,
    val strategy : ConstraintStrategy,
    val isActive : Boolean = true,
)


interface ConstraintStrategy{
    fun conflictWith(shift : Shift) : Boolean
}
