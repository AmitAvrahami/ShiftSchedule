package com.example.smartschedule.core.di

import com.example.smartschedule.core.domain.model.smartSchedule.enums.ShiftType
import com.example.smartschedule.feature.smartSchedule.domain.rules.composite.ScheduleValidator
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.ScheduleRule
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.hard.CriticalConstraintsRule
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.hard.DoubleBookingRule
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.hard.MandatoryShiftRule
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.hard.MinRestRule
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.soft.ConsecutiveNightsRule
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.soft.FairWorkloadRule
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.soft.MaxNightsPerWeekRule
import com.example.smartschedule.feature.smartSchedule.domain.rules.strategy.soft.WeeklyRuleRespectRule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ScheduleRulesModule {

    @Provides
    @Singleton
    fun provideScheduleRules(): Set<ScheduleRule> {
        return setOf(
            // --- Hard Rules (Critical) ---
            CriticalConstraintsRule() ,
            DoubleBookingRule(),
            MandatoryShiftRule() ,

            // כאן אנו קובעים את הקונפיגורציה של החוקים!
            // אם מחר הארגון יחליט שמנוחה מינימלית היא 10 שעות, משנים רק כאן.
            MinRestRule(
                minRestHoursHard = 8 ,
                recommendedRestHours = 16
            ) ,

            // --- Soft Rules (Warnings) ---
            ConsecutiveNightsRule(
                maxConsecutive = 2 ,
                nightShiftTypes = setOf(ShiftType.NIGHT) // הגדרה מה נחשב לילה
            ) ,
            MaxNightsPerWeekRule(maxNights = 2) ,
            FairWorkloadRule(allowedDeviation = 2),
        )
    }


    /**
     * הזרקת ה-Validator.
     * Hilt יחפש באופן אוטומטי מי מספק Set<ScheduleRule> (הפונקציה למעלה)
     * ויעביר אותו לכאן.
     */
    @Provides
    @Singleton
    fun provideScheduleValidator(
        // @JvmSuppressWildcards הוא קריטי ב-Kotlin כשמזריקים Collections גנריים
        rules: Set<@JvmSuppressWildcards ScheduleRule>
    ): ScheduleValidator {
        return ScheduleValidator(rules.toList())
    }

}