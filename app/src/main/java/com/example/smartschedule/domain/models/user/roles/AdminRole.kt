package com.example.smartschedule.domain.models.user.roles

import com.example.smartschedule.domain.models.user.permissions.Permission

class AdminRole : Role {
    override fun getRoleName(): String {
        return "Admin"
    }

    override fun getPermissions(): List<Permission> {
        return emptyList()
    }

    override fun addPermissions(permissions: List<Permission>) {
        TODO("Not yet implemented")
    }

    override fun hasPermission(permission: Permission): Boolean {
        TODO("Not yet implemented")
    }
}
