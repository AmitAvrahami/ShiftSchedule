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



