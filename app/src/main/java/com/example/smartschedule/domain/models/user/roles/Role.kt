package com.example.smartschedule.domain.models.user.roles

import com.example.smartschedule.domain.models.user.permissions.Permission

interface Role {
    fun getRoleName(): String
    fun getPermissions(): List<Permission>
    fun addPermissions(permissions: List<Permission>)
    fun hasPermission(permission: Permission): Boolean

}


