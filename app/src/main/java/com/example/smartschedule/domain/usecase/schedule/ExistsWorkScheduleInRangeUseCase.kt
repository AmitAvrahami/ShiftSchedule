package com.example.smartschedule.domain.usecase.schedule

import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.repository.workschedule.WorkScheduleRepository
import java.time.LocalDate
import javax.inject.Inject

class ExistsWorkScheduleInRangeUseCase @Inject constructor(
    private val workScheduleRepository: WorkScheduleRepository
) {
    suspend operator fun invoke(
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<Boolean> =
        workScheduleRepository.existsInRange(startDate, endDate)

}
