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
){
    fun isAssignedTo(employeeId: EmployeeId): Boolean = this.employeeId == employeeId
    fun isActiveAssignment(): Boolean = status == AssignmentStatus.ACTIVE
    fun isAssignedAndActiveTo(employeeId: EmployeeId): Boolean = isAssignedTo(employeeId) && isActiveAssignment()
}
