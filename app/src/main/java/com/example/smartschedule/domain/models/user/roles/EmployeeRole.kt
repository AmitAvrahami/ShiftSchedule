package com.example.smartschedule.domain.models.user.roles

import com.example.smartschedule.domain.models.constraint.Constraint
import com.example.smartschedule.domain.models.shift.Shift
import com.example.smartschedule.domain.models.user.permissions.Permission
import java.time.LocalDate

open class EmployeeRole(
    val constraints: List<Constraint> = emptyList(),
    val minShifts: Int = 3,
    val maxShifts: Int = 5,
    val hireDate: LocalDate,
    val notes: String? = null
) : Role {

    protected val permissions: MutableSet<Permission> = mutableSetOf()

    init {
        addEmployeePermissions()
    }

    private fun addEmployeePermissions() {
        permissions.addAll(
            listOf(
                Permission.VIEW_SCHEDULE,
                Permission.REQUEST_SHIFT_CHANGE
            )
        )
    }

    override fun getRoleName(): String = "Employee"

    override fun getPermissions(): List<Permission> = permissions.toList()

    override fun addPermissions(permissions: List<Permission>) {
        this.permissions.addAll(permissions)
    }

    override fun hasPermission(permission: Permission): Boolean {
        return permissions.contains(permission)
    }
}