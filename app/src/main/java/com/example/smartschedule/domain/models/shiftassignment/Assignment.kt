package com.example.smartschedule.domain.models.shiftassignment

import com.example.smartschedule.domain.models.shift.Shift
import com.example.smartschedule.domain.models.user.User
import com.example.smartschedule.domain.models.user.roles.EmployeeRole
import java.time.LocalDateTime

typealias UserId = String
data class Assignment(
    val id : Long ,
    val shift : Shift ,
    val assignedEmployees : List<UserId> ,
    val assignmentStatus : AssignmentStatus ,
    val assignedAt : LocalDateTime ,
    val assignedBy : EmployeeRole ,
    val notes : String? = null
)

enum class AssignmentStatus{
    PENDING,
    CONFIRMED,
    CANCELLED
}