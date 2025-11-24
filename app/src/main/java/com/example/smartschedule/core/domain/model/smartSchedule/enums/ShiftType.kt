package com.example.smartschedule.core.domain.model.smartSchedule.enums

    import java.time.LocalTime

    enum class ShiftType(
        val displayName: String,
        val defaultStartTime: LocalTime,
        val defaultEndTime: LocalTime,
        val isUnpopular: Boolean,
        val isWeekend: Boolean
    ) {
        MORNING(
            displayName = "משמרת בוקר",
            defaultStartTime = LocalTime.of(6, 45),
            defaultEndTime = LocalTime.of(14, 45),
            isUnpopular = false,
            isWeekend = false
        ),
        NOON(
            displayName = "משמרת צהריים",
            defaultStartTime = LocalTime.of(14, 45),
            defaultEndTime = LocalTime.of(22, 45),
            isUnpopular = false,
            isWeekend = false
        ),
        NIGHT(
            displayName = "משמרת לילה",
            defaultStartTime = LocalTime.of(22, 45),
            defaultEndTime = LocalTime.of(6, 45), // יום הבא – מטפלים בזה בלוגיקה
            isUnpopular = true,
            isWeekend = false
        );
    }
