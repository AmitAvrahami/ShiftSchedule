package com.example.smartschedule.feature.smartSchedule.domain.rules.strategy

import com.example.smartschedule.core.domain.model.constraints.Constraint
import com.example.smartschedule.core.domain.model.constraints.WeeklyRule
import com.example.smartschedule.core.domain.model.smartSchedule.WorkSchedule
import com.example.smartschedule.feature.smartSchedule.domain.rules.models.RuleViolation

interface ScheduleRule {
    val id: String
    val name: String

    fun validate(
        schedule: WorkSchedule ,
        constraints: List<Constraint>,
        weeklyRules: List<WeeklyRule>
    ): List<RuleViolation>
}
