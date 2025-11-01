package com.example.smartschedule.data.mapper

import com.example.smartschedule.domain.models.user.permissions.Permission
import com.example.smartschedule.domain.models.user.roles.*
import java.time.LocalDate

object RoleMapper {

    fun toMap(role: Role): Map<String, Any> {
        val baseMap = mutableMapOf<String, Any>(
            "type" to role.getRole().name,
            "permissions" to role.getPermissions().map { it.name }
        )

        when (role) {
            is ManagerRole -> {
                baseMap["hireDate"] = role.hireDate.toString()
                baseMap["minShifts"] = role.minShifts
                baseMap["maxShifts"] = role.maxShifts
                baseMap["notes"] = role.notes ?: ""
            }

            is EmployeeRole -> {
                baseMap["hireDate"] = role.hireDate.toString()
                baseMap["minShifts"] = role.minShifts
                baseMap["maxShifts"] = role.maxShifts
                baseMap["notes"] = role.notes ?: ""
            }

            is AdminRole -> {
            }
        }

        return baseMap
    }

    fun fromMap(map: Map<String, Any>): Role {
        val type = map["type"] as? String ?: "EMPLOYEE"
        val hireDateStr = map["hireDate"] as? String ?: LocalDate.now().toString()
        val hireDate = LocalDate.parse(hireDateStr)
        val minShifts = (map["minShifts"] as? Long ?: 3L).toInt()
        val maxShifts = (map["maxShifts"] as? Long ?: 5L).toInt()
        val notes = map["notes"] as? String

        return when (type) {
            "MANAGER" -> ManagerRole(
                hireDate = hireDate,
                minShifts = minShifts,
                maxShifts = maxShifts,
                notes = notes
            )
            "ADMIN" -> AdminRole()
            else -> EmployeeRole(
                hireDate = hireDate,
                minShifts = minShifts,
                maxShifts = maxShifts,
                notes = notes
            )
        }
    }

    inline fun <reified K, reified V> Any?.asMapOrEmpty(): Map<K, V> {
        return if (this is Map<*, *>) {
            this.filterKeys { it is K }
                .mapKeys { it.key as K }
                .mapValues { it.value as V }
        } else {
            emptyMap()
        }
    }
}
