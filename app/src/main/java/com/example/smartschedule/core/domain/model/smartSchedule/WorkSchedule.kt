package com.example.smartschedule.core.domain.model.smartSchedule

import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.enums.AssignmentStatus
import com.example.smartschedule.core.domain.model.smartSchedule.enums.BoardStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import kotlin.collections.mapNotNull

@JvmInline
value class WorkScheduleId(val value: String)

data class WorkSchedule(
    val id: WorkScheduleId,
    val name: String,
    val period: ClosedRange<LocalDate>,
    val createdBy: EmployeeId,
    val status: BoardStatus ,
    val notes: String?,
    val shifts: List<Shift>,
    val assignments: List<ShiftAssignment>,
    val creationDate: LocalDateTime,
    val updateDate: LocalDateTime?
){

    fun getAssignmentsForShift(shiftId: ShiftId): List<ShiftAssignment> =
        assignments.filter { it.shiftId == shiftId && it.status == AssignmentStatus.ACTIVE }

    fun assignEmployee(shiftId: ShiftId, employeeId: EmployeeId): WorkSchedule {
        val newAssignment = ShiftAssignment(
            id = ShiftAssignmentId(UUID.randomUUID().toString()), // MVP, אחר כך תחליף לג׳נרטור רציני
            workScheduleId = id,
            shiftId = shiftId,
            employeeId = employeeId,
            status = AssignmentStatus.ACTIVE
        )

        return copy(assignments = assignments + newAssignment)
    }

    fun unassignEmployee(shiftId: ShiftId, employeeId: EmployeeId): WorkSchedule {
        val updated = assignments.map {
            if (it.shiftId == shiftId && it.employeeId == employeeId && it.status == AssignmentStatus.ACTIVE) {
                it.copy(status = AssignmentStatus.CANCELED)
            } else it
        }
        return copy(assignments = updated)
    }

    fun getAssignmentsForEmployee(employeeId: EmployeeId): List<ShiftAssignment> =
        assignments.filter { assignment -> assignment.isAssignedAndActiveTo(employeeId) }


}
