package com.example.smartschedule.domain.models.user.roles

import com.example.smartschedule.domain.models.user.permissions.Permission

class AdminRole : Role {

    override fun getRole(): Roles = Roles.ADMIN

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
