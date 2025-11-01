package com.example.smartschedule.domain.models.user.roles

import com.example.smartschedule.domain.models.constraint.Constraint
import com.example.smartschedule.domain.models.shift.Shift
import com.example.smartschedule.domain.models.user.permissions.Permission
import java.time.LocalDate

class ManagerRole(
    constraints: List<Constraint> = emptyList(),
    assignedShifts: List<Shift> = emptyList(),
    minShifts: Int = 3,
    maxShifts: Int = 5,
    hireDate: LocalDate,
    notes: String? = null,
) : EmployeeRole(constraints, minShifts, maxShifts, hireDate, notes) {

    init {
        addManagerPermissions()
    }

    private fun addManagerPermissions() {
        permissions.addAll(
            listOf(
                Permission.APPROVE_REQUESTS,
                Permission.ASSIGN_SHIFTS,
            )
        )
    }

    override fun getRole() : Roles = Roles.MANAGER
}