package com.example.smartschedule.data.mapper

import com.example.smartschedule.domain.models.user.roles.EmployeeRole
import com.example.smartschedule.domain.models.user.roles.ManagerRole
import com.example.smartschedule.domain.models.workschedule.ScheduleStatus
import com.example.smartschedule.domain.models.workschedule.WorkSchedule
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

object WorkScheduleMapper {

    fun toMap(schedule: WorkSchedule): Map<String, Any?> {
        return mapOf(
            "id" to schedule.id,
            "title" to schedule.title,
            "startDate" to schedule.startDate.toEpochDay(),
            "endDate" to schedule.endDate.toEpochDay(),
            "status" to schedule.status.name,
            "assignments" to schedule.assignments.map { AssignmentMapper.toMap(it) },
            "createdAt" to schedule.createdAt.toEpochSecond(ZoneOffset.UTC),
            "updatedAt" to schedule.updatedAt.toEpochSecond(ZoneOffset.UTC),
            "createdBy" to RoleMapper.toMap(schedule.createdBy),
            "approveBy" to RoleMapper.toMap(schedule.approveBy),
            "notes" to schedule.notes
        )
    }

    fun fromMap(data: Map<String, Any?>): WorkSchedule {
        val startDate = LocalDate.ofEpochDay((data["startDate"] as Number).toLong())
        val endDate = LocalDate.ofEpochDay((data["endDate"] as Number).toLong())
        val createdAt = LocalDateTime.ofInstant(
            Instant.ofEpochSecond((data["createdAt"] as Number).toLong()),
            ZoneOffset.UTC
        )
        val updatedAt = LocalDateTime.ofInstant(
            Instant.ofEpochSecond((data["updatedAt"] as Number).toLong()),
            ZoneOffset.UTC
        )

        val assignmentsList = (data["assignments"] as? List<Map<String, Any?>>)
            ?.map { AssignmentMapper.fromMap(it) }
            ?: emptyList()

        val createdBy = RoleMapper.fromMap(data["createdBy"] as? Map<String, Any> ?: emptyMap())
        val approvedBy = RoleMapper.fromMap(data["approveBy"] as? Map<String, Any> ?: emptyMap())

        return WorkSchedule(
            id = (data["id"] as Number).toLong(),
            title = data["title"] as String,
            startDate = startDate,
            endDate = endDate,
            status = ScheduleStatus.valueOf(data["status"] as String),
            assignments = assignmentsList,
            createdAt = createdAt,
            updatedAt = updatedAt,
            createdBy = createdBy as EmployeeRole,
            approveBy = approvedBy as ManagerRole,
            notes = data["notes"] as? String
        )
    }
}
