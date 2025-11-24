package com.example.smartschedule.rules

import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.Shift
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftAssignment
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftAssignmentId
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftId
import com.example.smartschedule.core.domain.model.smartSchedule.WorkSchedule
import com.example.smartschedule.core.domain.model.smartSchedule.enums.AssignmentStatus
import com.example.smartschedule.core.domain.model.smartSchedule.enums.BoardStatus
import com.example.smartschedule.core.domain.model.smartSchedule.enums.ShiftType
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.ViolationSeverity
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.hard.DoubleBookingRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class DoubleBookingRule {

    private val rule = DoubleBookingRule()
    private val employeeId = EmployeeId(1)

    private fun createScheduleWithShifts(
        vararg shiftsTimes: Pair<LocalTime , LocalTime> // מקבל זוגות של התחלה-סוף
    ): WorkSchedule {
        val date = LocalDate.now()
        val shifts = mutableListOf<Shift>()
        val assignments = mutableListOf<ShiftAssignment>()

        shiftsTimes.forEachIndexed { index, (start, end) ->
            val shiftId = ShiftId(index)
            shifts.add(
                Shift.fromType(
                    id = shiftId,
                    date = date,
                    shiftType = ShiftType.MORNING,
                    notes = null
                )
            )
            assignments.add(
                ShiftAssignment(
                    id = ShiftAssignmentId(index) ,
                    shiftId = shiftId,
                    employeeId = employeeId,
                    status = AssignmentStatus.ACTIVE,
                    workScheduleId = 1
                )
            )
        }

        return WorkSchedule(
            id = 1,
            shifts = shifts,
            assignments = assignments,
            period = date..date,
            name = "Test",
            createdBy = 1,
            status = BoardStatus.DRAFT,
            creationDate = LocalDateTime.now(),
            updateDate = null,
            notes = null
        )
    }

    @Test
    fun `validate detects overlap when second shift starts before first ends`() {
        // 1. Arrange
        val schedule = createScheduleWithShifts(
            LocalTime.of(8, 0) to LocalTime.of(16, 0),  // משמרת 1: מסתיימת ב-16:00
            LocalTime.of(14, 0) to LocalTime.of(22, 0)  // משמרת 2: מתחילה ב-14:00
        )

        // 2. Act
        val violations = rule.validate(schedule, emptyList(), emptyList())

        // 3. Assert
        assertEquals("אמורה להיות שגיאה אחת של חפיפה", 1, violations.size)
        assertEquals(ViolationSeverity.ERROR, violations[0].severity)
    }


    @Test
    fun `validate allows shifts that touch but do not overlap`() {
        // 1. Arrange
        val schedule = createScheduleWithShifts(
            LocalTime.of(8, 0) to LocalTime.of(16, 0),
            LocalTime.of(16, 0) to LocalTime.of(23, 0)
        )
        val emptySchedule = schedule.copy(assignments = emptyList())

        // 2. Act
        val violations = rule.validate(emptySchedule, emptyList(), emptyList())

        // 3. Assert
        assertTrue("לא אמורות להיות שגיאות בחפיפת קצה", violations.isEmpty())
    }

    @Test
    fun `validate detects overlap when night shift spills into next day`() {
        // 1. Arrange
        val schedule = createScheduleWithShifts(
            LocalTime.of(22, 0) to LocalTime.of(6, 0), // גולש ליום המחרת!
            LocalTime.of(5, 0) to LocalTime.of(13, 0)  // מתחיל ב-5 בבוקר (חופף לשעה האחרונה של הלילה)
        )

        // 2. Act
        val violations = rule.validate(schedule, emptyList(), emptyList())

        // 3. Assert
        assertEquals("החוק אמור לזהות חפיפה בגלל גלישה ליום המחרת", 1, violations.size)
    }


}