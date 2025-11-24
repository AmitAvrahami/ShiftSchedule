package com.example.smartschedule.feature.smartSchedule.data.repository

import com.example.smartschedule.core.domain.model.constraints.Constraint
import com.example.smartschedule.core.domain.model.constraints.WeeklyRule
import com.example.smartschedule.core.domain.model.employees.* // ייבוא המודל וה-Enums שלך
import com.example.smartschedule.core.domain.model.employees.enums.EmployeeRole
import com.example.smartschedule.core.domain.model.employees.enums.EmploymentType
import com.example.smartschedule.core.domain.model.smartSchedule.*
import com.example.smartschedule.core.domain.model.smartSchedule.enums.*
import com.example.smartschedule.feature.smartSchedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class FakeScheduleRepository @Inject constructor() : ScheduleRepository {

    private val today = LocalDate.now()

    private val _scheduleFlow = MutableStateFlow(createInitialMockSchedule())


    private val employees = listOf(
        Employee(
            id = EmployeeId(1) ,
            fullName = "דני כהן" ,
            role = EmployeeRole.EMPLOYEE ,
            isActive = true ,
            preferredShiftTypeIds = listOf(
                1 ,
                2
            ) , // מעדיף בוקר/ערב
            employmentType = EmploymentType.FULL_TIME
        ) ,
        Employee(
            id = EmployeeId(2) ,
            fullName = "מיכל לוי" ,
            role = EmployeeRole.EMPLOYEE ,
            isActive = true ,
            preferredShiftTypeIds = listOf(2) , // מעדיפה רק ערב
            employmentType = EmploymentType.PART_TIME
        ) ,
        Employee(
            id = EmployeeId(3) ,
            fullName = "רונן בר" ,
            role = EmployeeRole.EMPLOYEE ,
            isActive = true ,
            preferredShiftTypeIds = emptyList() ,
            employmentType = EmploymentType.PART_TIME
        )
    )

    private fun createInitialMockSchedule(): WorkSchedule {
        val shifts = listOf(
            // יום שני: משמרת בוקר (08-16)
            createShift(
                1 ,
                today ,
                ShiftType.MORNING ,
                8 ,
                16
            ) ,
            // יום שני: משמרת ערב (15-23) -> חפיפה של שעה!
            createShift(
                2 ,
                today ,
                ShiftType.NOON ,
                15 ,
                23
            ) ,
            // יום שלישי: משמרת לילה תקינה
            createShift(
                3 ,
                today.plusDays(1) ,
                ShiftType.NIGHT ,
                22 ,
                6
            )
        )

        val assignments = listOf(
            // דני (עובד 1) עובד בוקר
            createAssignment(
                101 ,
                1 ,
                1
            ) ,
            // דני (עובד 1) משובץ גם לערב -> Error: Overlapping Shifts!
            createAssignment(
                102 ,
                2 ,
                1
            ) ,

            // מיכל (עובדת 2) עובדת בערב (תקין)
            createAssignment(
                103 ,
                2 ,
                2
            )
        )

        return WorkSchedule(
            id = 1 ,
            name = "סידור שבועי" ,
            period = today .. today.plusDays(6) ,
            shifts = shifts ,
            assignments = assignments ,
            status = BoardStatus.DRAFT ,
            createdBy = 999 ,
            creationDate = LocalDateTime.now() ,
            updateDate = null ,
            notes = null
        )
    }

    override fun getWorkSchedule(scheduleId : String) : Flow<WorkSchedule> = _scheduleFlow


    override fun getEmployees() : Flow<List<Employee>> = flowOf(employees)

    override fun getConstraints(start : Long , end : Long) : Flow<List<Constraint>> =
        flowOf(emptyList())

    override fun getWeeklyRules() : Flow<List<WeeklyRule>> = flowOf(emptyList())

    override suspend fun updateSchedule(schedule: WorkSchedule): Result<Unit> {
        _scheduleFlow.value = schedule
        return Result.success(Unit)
}

    // Helpers
    private fun createShift(
        id : Int ,
        date : LocalDate ,
        type : ShiftType ,
        start : Int ,
        end : Int
    ) = Shift(
        id = ShiftId(id) ,
        date = date ,
        startTime = LocalTime.of(
            start ,
            0
        ) ,
        endTime = LocalTime.of(
            end ,
            0
        ) ,
        shiftType = type ,
        notes = null
    )

    private fun createAssignment(id : Int , shiftId : Int , empId : Int) = ShiftAssignment(
        id = ShiftAssignmentId(id) ,
        shiftId = ShiftId(shiftId) ,
        employeeId = EmployeeId(empId) ,
        status = AssignmentStatus.ACTIVE ,
        workScheduleId = 1
    )
}