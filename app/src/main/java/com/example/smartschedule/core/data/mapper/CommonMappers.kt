package com.example.smartschedule.core.data.mapper

import com.example.smartschedule.core.data.firebase.model.AssignmentDto
import com.example.smartschedule.core.data.firebase.model.ConstraintDto
import com.example.smartschedule.core.data.firebase.model.ScheduleDto
import com.example.smartschedule.core.data.firebase.model.ShiftDto
import com.example.smartschedule.core.data.firebase.model.UserDto
import com.example.smartschedule.core.data.firebase.model.WeeklyRuleDto
import com.example.smartschedule.core.domain.model.constraints.Constraint
import com.example.smartschedule.core.domain.model.constraints.ConstraintId
import com.example.smartschedule.core.domain.model.constraints.WeeklyRule
import com.example.smartschedule.core.domain.model.constraints.WeeklyRuleId
import com.example.smartschedule.core.domain.model.constraints.enums.ConstraintType
import com.example.smartschedule.core.domain.model.constraints.enums.RuleKind
import com.example.smartschedule.core.domain.model.employees.Employee
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.employees.enums.EmployeeRole
import com.example.smartschedule.core.domain.model.employees.enums.EmploymentType
import com.example.smartschedule.core.domain.model.smartSchedule.Shift
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftAssignment
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftAssignmentId
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftId
import com.example.smartschedule.core.domain.model.smartSchedule.WorkSchedule
import com.example.smartschedule.core.domain.model.smartSchedule.WorkScheduleId
import com.example.smartschedule.core.domain.model.smartSchedule.enums.AssignmentStatus
import com.example.smartschedule.core.domain.model.smartSchedule.enums.BoardStatus
import com.example.smartschedule.core.domain.model.smartSchedule.enums.ShiftType
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

inline fun <T> String.tryParse(
    parse: (String) -> T,
    defaultValue: () -> T
) : T {
    return try {
        parse(this)
    } catch (e: IllegalArgumentException) {
        defaultValue()
    }
}

//From DTO to Domain
fun UserDto.toDomain() : Employee{
    val id = EmployeeId(this.userId)
    val role = try{ EmployeeRole.valueOf(this.role) }catch (e: Exception){ EmployeeRole.EMPLOYEE }
    val employmentType = try { EmploymentType.valueOf(this.employmentType) }catch (e: Exception){ EmploymentType.FULL_TIME }
    return Employee(
        id = id ,
        fullName = "${this.firstName} ${this.lastName}",
        role = role ,
        isActive = this.isActive ,
        preferredShiftTypeIds = this.preferredShiftTypeIds ,
        employmentType = employmentType
    )
}
fun ScheduleDto.toDomain(): WorkSchedule {
    val start = startDate.tryParse(
        { LocalDate.parse(it) } ,
        { LocalDate.now() })
    val end = endDate.tryParse({ LocalDate.parse(it) } ,
        { LocalDate.now() })

    val domainShifts = shifts.map { it.toDomainShift() }


    val domainAssignments = shifts.flatMap { shiftDto ->
        shiftDto.assignments.map { assignmentDto ->
            assignmentDto.toDomainAssignment(
                shiftDto.shiftId ,
                scheduleId
            )
        }
    }

    return WorkSchedule(
        id = WorkScheduleId(scheduleId) ,
        name = name ,
        period = start .. end ,
        status = status.tryParse({ BoardStatus.valueOf(it) } ,
            { BoardStatus.DRAFT }) ,
        notes = notes ,
        shifts = domainShifts ,
        assignments = domainAssignments ,
        createdBy = EmployeeId(createdBy) ,
        creationDate = LocalDateTime.now() , // או המרה מ-Long
        updateDate = updateDate?.let { LocalDateTime.now() } // פשטות כרגע
    )
}
fun ShiftDto.toDomainShift(): Shift {
    return Shift(
        id = ShiftId(shiftId) ,
        date = date.tryParse(
            { LocalDate.parse(date) } ,
            { LocalDate.now() }) ,
        startTime = startTime.tryParse(
            { LocalTime.parse(startTime) } ,
            { LocalTime.MIN }) ,
        endTime = endTime.tryParse(
            { LocalTime.parse(endTime) } ,
            { LocalTime.MAX }) ,
        shiftType = type.tryParse(
            { ShiftType.valueOf(it) } ,
            { ShiftType.MORNING }) ,
        notes = notes ,
//        requiredEmployees = requiredEmployees,
//        definitionId = definitionId
    )
}
fun AssignmentDto.toDomainAssignment(shiftIdStr: String, scheduleIdStr: String): ShiftAssignment {
    return ShiftAssignment(
        id = ShiftAssignmentId(assignmentId) ,
        shiftId = ShiftId(shiftIdStr) ,
        employeeId = EmployeeId(employeeId) ,
        status = status.tryParse(
            { AssignmentStatus.valueOf(it) } ,
            { AssignmentStatus.ACTIVE }) ,
        workScheduleId = WorkScheduleId(scheduleIdStr)
    )
}
fun ConstraintDto.toDomain(): Constraint {
    return Constraint(
        id = ConstraintId(constraintId),
        employeeId = EmployeeId(employeeId),

        startDate = startDate.tryParse({ LocalDate.parse(it) }, { LocalDate.now()}),

        endDate = endDate.tryParse({ LocalDate.parse(it) }, { LocalDate.now()}),


        startTime = startTime?.let {it.tryParse({ LocalTime.parse(it) }, { LocalTime.now()})},

        endTime = endTime?.let {it.tryParse({ LocalTime.parse(it) }, { LocalTime.now()})},

        type = type.tryParse({ ConstraintType.valueOf(it)},
            { ConstraintType.FULL_DAY }),

        reason = reason
    )


}

fun WeeklyRuleDto.toDomain(): WeeklyRule {
    return WeeklyRule(
        id = WeeklyRuleId(ruleId),
        employeeId = EmployeeId(employeeId),
        dayOfWeek = dayOfWeek.tryParse({ DayOfWeek.valueOf(it) }, { DayOfWeek.MONDAY } ),
        shiftType = shiftType.tryParse({ ShiftType.valueOf(it) }, { ShiftType.MORNING }),
        kind = kind.tryParse({ RuleKind.valueOf(it) }, { RuleKind.UNAVAILABLE }),
        isActive = isActive,
        notes = notes
    )
}




//From Domain to DTO
fun Employee.toDto(
    email: String = "",
    colorHex: String = "#808080",
    phoneNumber: String? = null,
    avatar: String? = null
): UserDto {

    val nameParts = this.fullName.trim().split(" ", limit = 2)
    val fName = nameParts.getOrElse(0) { "" }
    val lName = nameParts.getOrElse(1) { "" }

    return UserDto(
        userId = this.id.value,
        firstName = fName,
        lastName = lName,

        email = email,
        phoneNumber = phoneNumber,
        avatar = avatar,

        role = this.role.name,
        employmentType = this.employmentType.name,

        isActive = this.isActive,
        colorHex = colorHex,
        preferredShiftTypeIds = this.preferredShiftTypeIds,

        updatedAt = System.currentTimeMillis()
    )
}

fun WorkSchedule.toDto(): ScheduleDto{

    val assignmentsByShiftMap = this.assignments.groupBy { it.shiftId }
    val shiftDtos = this.shifts.map { shift ->
        val assignments = assignmentsByShiftMap[shift.id] ?: emptyList()
        shift.toDto(assignments)
    }
    return ScheduleDto(
        scheduleId = this.id.value,
        name = this.name,
        startDate = this.period.start.toString(),
        endDate = this.period.endInclusive.toString(),
        status = this.status.name,
        notes = this.notes,
        shifts = shiftDtos,
        createdBy = this.createdBy.value,
        updateDate = System.currentTimeMillis()
    )
}

fun Shift.toDto(assignments: List<ShiftAssignment>): ShiftDto{
    return ShiftDto(
        shiftId = this.id.value,
        date = this.date.toString(),
        startTime = this.startTime.toString(),
        endTime = this.endTime.toString(),
        type = this.shiftType.name,
        requiredEmployees = 2, //Todo : Add Required Employees To Domain
        assignments = assignments.map { it.toDto() },
        notes = this.notes
    )
}

fun ShiftAssignment.toDto(): AssignmentDto {
    return AssignmentDto(
        assignmentId = this.id.value,
        employeeId = this.employeeId.value,
        status = this.status.name,
    )
}

fun Constraint.toDto(employeeName: String):ConstraintDto{
    return ConstraintDto(
        constraintId = this.id.value,
        employeeId = this.employeeId.value,
        employeeName = employeeName,        // השם מגיע מבחוץ (מה-ViewModel או ה-Repo)

        startDate = this.startDate.toString(),
        endDate = this.endDate.toString(),

        startTime = this.startTime?.toString(),
        endTime = this.endTime?.toString(),

        type = this.type.name,
        reason = this.reason,

        updatedAt = System.currentTimeMillis()
    )
}

fun WeeklyRule.toDto(): WeeklyRuleDto {
    return WeeklyRuleDto(
        ruleId = id.value,
        employeeId = employeeId.value,

        dayOfWeek = dayOfWeek.name,
        shiftType = shiftType.name,
        kind = kind.name,

        isActive = isActive,
        notes = notes
    )
}