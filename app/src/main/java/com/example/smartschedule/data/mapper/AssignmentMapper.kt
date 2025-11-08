package com.example.smartschedule.data.mapper

import com.example.smartschedule.data.mapper.RoleMapper.asMapOrEmpty
import com.example.smartschedule.domain.models.shiftassignment.Assignment
import com.example.smartschedule.domain.models.shiftassignment.AssignmentStatus
import com.example.smartschedule.domain.models.shift.Shift
import com.example.smartschedule.domain.models.user.roles.EmployeeRole
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

object AssignmentMapper {

    fun toMap(assignment: Assignment): Map<String, Any?> {
        return mapOf(
            "id" to assignment.id,
            "shift" to ShiftMapper.toMap(assignment.shift),
            "assignedEmployees" to assignment.assignedEmployees.map { it },
            "assignmentStatus" to assignment.assignmentStatus.name,
            "assignedAt" to assignment.assignedAt.toEpochSecond(ZoneOffset.UTC),
            "assignedBy" to RoleMapper.toMap(assignment.assignedBy),
            "notes" to assignment.notes
        )
    }

    fun fromMap(data: Map<String, Any?>): Assignment {
        val assignedAt = LocalDateTime.ofInstant(
            Instant.ofEpochSecond((data["assignedAt"] as Number).toLong()),
            ZoneOffset.UTC
        )

        val shift = ShiftMapper.fromMap(data["shift"].asMapOrEmpty<String, Any>())
        val assignedBy = RoleMapper.fromMap(data["assignedBy"].asMapOrEmpty<String, Any>())

        return Assignment(
            id = (data["id"] as Number).toLong(),
            shift = shift,
            assignedEmployees = (data["assignedEmployees"] as? List<String>)?.map { it } ?: emptyList(),
            assignmentStatus = AssignmentStatus.valueOf(data["assignmentStatus"] as String),
            assignedAt = assignedAt,
            assignedBy = assignedBy as EmployeeRole,
            notes = data["notes"] as? String
        )
    }
}
