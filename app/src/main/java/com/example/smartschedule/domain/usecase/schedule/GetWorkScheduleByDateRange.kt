package com.example.smartschedule.domain.usecase.schedule

import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.models.workschedule.WorkSchedule
import com.example.smartschedule.domain.repository.workschedule.WorkScheduleRepository
import java.time.LocalDate
import javax.inject.Inject

class GetWorkScheduleByDateRange @Inject constructor(
    private val workScheduleRepository: WorkScheduleRepository
) {
    suspend operator fun invoke(
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<List<WorkSchedule>> =
        workScheduleRepository.getWorkSchedulesByDateRange(startDate, endDate)

}
