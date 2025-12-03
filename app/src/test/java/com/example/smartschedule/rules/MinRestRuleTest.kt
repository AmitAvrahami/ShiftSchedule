package com.example.smartschedule.feature.smartSchedule.domain.rules

import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.*
import com.example.smartschedule.core.domain.model.smartSchedule.enums.*
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.ViolationSeverity
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.hard.MinRestRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class MinRestRuleTest {

    // מגדירים את החוק עם ערכים ידועים לטסט: 8 שעות קריטי, 12 שעות מומלץ (כדי שיהיה קל לבדוק)
    private val rule = MinRestRule(minRestHoursHard = 8, recommendedRestHours = 12)
    private val employeeId = EmployeeId("1")

    // --- הטסטים ---

    @Test
    fun `validate returns error when rest is less than 8 hours`() {
        // תרחיש: סיים ב-23:00 בלילה, מתחיל ב-06:00 בבוקר (רק 7 שעות מנוחה)
        val schedule = createScheduleWithDays(
            ShiftData(dayOffset = 0, start = LocalTime.of(15, 0), end = LocalTime.of(23, 0) ,
                ShiftType.NOON),
            ShiftData(dayOffset = 1, start = LocalTime.of(6, 0), end = LocalTime.of(14, 0),
                ShiftType.MORNING)
        )

        val violations = rule.validate(schedule, emptyList(), emptyList())

        assertEquals("צריכה להיות הפרה אחת", 1, violations.size)
        assertEquals("החריגה צריכה להיות קריטית (ERROR)", ViolationSeverity.ERROR, violations[0].severity)
    }

    @Test
    fun `validate returns warning when rest is between 8 and 12 hours`() {
        // תרחיש: סיים ב-22:00 בלילה, מתחיל ב-08:00 בבוקר (10 שעות מנוחה)
        // זה יותר מ-8 (תקין חוקית) אבל פחות מ-12 (לא מומלץ) -> אזהרה
        val schedule = createScheduleWithDays(
            ShiftData(dayOffset = 0, start = LocalTime.of(14, 0), end = LocalTime.of(22, 0)),
            ShiftData(dayOffset = 1, start = LocalTime.of(8, 0), end = LocalTime.of(16, 0))
        )

        val violations = rule.validate(schedule, emptyList(), emptyList())

        assertEquals(1, violations.size)
        assertEquals("החריגה צריכה להיות אזהרה (WARNING)", ViolationSeverity.WARNING, violations[0].severity)
    }

    @Test
    fun `validate passes when rest is sufficient`() {
        // תרחיש: סיים ב-16:00, מתחיל ב-08:00 למחרת (16 שעות מנוחה) -> מצוין
        val schedule = createScheduleWithDays(
            ShiftData(dayOffset = 0, start = LocalTime.of(8, 0), end = LocalTime.of(16, 0)),
            ShiftData(dayOffset = 1, start = LocalTime.of(8, 0), end = LocalTime.of(16, 0))
        )

        val violations = rule.validate(schedule, emptyList(), emptyList())

        assertTrue("לא אמורות להיות שגיאות", violations.isEmpty())
    }

    @Test
    fun `validate handles same day shifts correctly`() {
        // מקרה קיצון: "פיצול" - משמרת בוקר ואז משמרת ערב באותו יום
        // סיים ב-12:00, חוזר ב-18:00 (6 שעות מנוחה) -> קריטי
        val schedule = createScheduleWithDays(
            ShiftData(dayOffset = 0, start = LocalTime.of(8, 0), end = LocalTime.of(12, 0)),
            ShiftData(dayOffset = 0, start = LocalTime.of(18, 0), end = LocalTime.of(22, 0))
        )

        val violations = rule.validate(schedule, emptyList(), emptyList())

        assertEquals(1, violations.size)
        assertEquals(ViolationSeverity.ERROR, violations[0].severity)
    }

    // --- עזרים לטסט ---

    // מבנה נתונים קטן כדי להקל על הקריאה של הטסט
    data class ShiftData(val dayOffset: Int, val start: LocalTime, val end: LocalTime , val type
    : ShiftType = ShiftType.MORNING)

    private fun createScheduleWithDays(vararg shiftsData: ShiftData): WorkSchedule {
        val baseDate = LocalDate.now()
        val shifts = mutableListOf<Shift>()
        val assignments = mutableListOf<ShiftAssignment>()

        shiftsData.forEachIndexed { index, data ->
            val shiftId = ShiftId("$index")
            shifts.add(
                Shift(
                    shiftId,
                    baseDate.plusDays(data.dayOffset.toLong()),
                    shiftType = data.type,
                    startTime = data.start,
                    endTime = data.end,
                    notes = null
                )
            )
            assignments.add(
                ShiftAssignment(
                    id = ShiftAssignmentId("$index"),
                    shiftId = shiftId,
                    employeeId = employeeId,
                    status = AssignmentStatus.ACTIVE, workScheduleId = WorkScheduleId("1")
                )
            )
        }

        return WorkSchedule(
            id = WorkScheduleId("1"), shifts = shifts, assignments = assignments,
            period = baseDate..baseDate.plusDays(7), name = "Test", createdBy = EmployeeId("1"),
            status = BoardStatus.DRAFT, creationDate = LocalDateTime.now(),
            updateDate = null, notes = null
        )
    }
}