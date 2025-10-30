package com.example.smartschedule.domain.models.constraint

import androidx.compose.animation.core.AnimationEndReason
import com.example.smartschedule.domain.models.employee.Employee
import com.example.smartschedule.domain.models.shift.Shift
import com.example.smartschedule.domain.models.user.roles.EmployeeRole

data class Constraint(
    val id : Long,
    val employee: EmployeeRole,
    val reason: String? = null,
    val strategy : ConstraintStrategy,
    val isActive : Boolean = true,
){
    fun activate() : Constraint = copy(isActive = true)
    fun deactivate() : Constraint = copy(isActive = false)
}

interface ConstraintStrategy{
    fun conflictWith(shift : Shift) : Boolean
}


class FullDayConstraintStrategy : ConstraintStrategy{
    override fun conflictWith(shift: Shift): Boolean {
        TODO("Not yet implemented")
    }

}

class ShiftConstraintStrategy  : ConstraintStrategy {
    override fun conflictWith(shift: Shift): Boolean {
        TODO("Not yet implemented")
    }
}

class DateRangeConstraintStrategy : ConstraintStrategy {
    override fun conflictWith(shift: Shift): Boolean {
        TODO("Not yet implemented")
    }
}

class RecurringConstraintStrategy : ConstraintStrategy {
    override fun conflictWith(shift: Shift): Boolean {
        TODO("Not yet implemented")
    }
}