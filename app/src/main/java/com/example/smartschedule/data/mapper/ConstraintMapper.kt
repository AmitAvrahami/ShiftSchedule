package com.example.smartschedule.data.mapper

import com.example.smartschedule.data.mapper.RoleMapper.asMapOrEmpty
import com.example.smartschedule.domain.models.constraint.*
import com.example.smartschedule.domain.models.shift.Shift
import com.example.smartschedule.domain.models.user.roles.EmployeeRole
import java.time.LocalDateTime
import java.time.ZoneOffset

object ConstraintMapper {

    fun toMap(constraint: Constraint): Map<String, Any?> {
        return mapOf(
            "id" to constraint.id,
            "employee" to RoleMapper.toMap(constraint.employee),
            "reason" to constraint.reason,
            "strategyType" to constraint.strategy::class.simpleName,
            "isActive" to constraint.isActive
        )
    }

    fun fromMap(data: Map<String, Any?>): Constraint {
        val employee = RoleMapper.fromMap(data["employee"].asMapOrEmpty<String, Any>())
        val strategyType = data["strategyType"] as? String ?: "FullDayConstraintStrategy"
        val strategy = ConstraintStrategyFactory.create(strategyType)

        return Constraint(
            id = (data["id"] as Number).toLong(),
            employee = employee as EmployeeRole,
            reason = data["reason"] as? String,
            strategy = strategy,
            isActive = data["isActive"] as? Boolean ?: true
        )
    }
}
