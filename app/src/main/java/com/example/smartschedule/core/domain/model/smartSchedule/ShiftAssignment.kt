package com.example.smartschedule.core.domain.model.smartSchedule

import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.enums.AssignmentStatus

@JvmInline
value class ShiftAssignmentId(val value: Int)

data class ShiftAssignment(
    val id: ShiftAssignmentId,
    val workScheduleId: Int,
    val shiftId: ShiftId,
    val employeeId: EmployeeId ,
    val status: AssignmentStatus
)
