package com.example.smartschedule.core.data.firebase.model

import com.example.smartschedule.core.data.mapper.tryParse
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftAssignment
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftAssignmentId
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftId
import com.example.smartschedule.core.domain.model.smartSchedule.WorkScheduleId
import com.example.smartschedule.core.domain.model.smartSchedule.enums.AssignmentStatus

data class AssignmentDto(
    val assignmentId: String = "",
    val employeeId: String = "",
    val status: String = AssignmentStatus.ACTIVE.name,

    val employeeName: String = "",
    val employeeColorHex: String = "#CCCCCC",
    val employeeRole: String = ""
)


fun AssignmentDto.toDomainAssignment(shiftIdStr: String, scheduleIdStr: String): ShiftAssignment {
    return ShiftAssignment(
        id = ShiftAssignmentId(assignmentId) ,
        shiftId = ShiftId(shiftIdStr) , 
        employeeId = EmployeeId(employeeId) ,
        status =  status.tryParse({ AssignmentStatus.valueOf(it) }, { AssignmentStatus.ACTIVE }),
        workScheduleId = WorkScheduleId(scheduleIdStr)
    )
}