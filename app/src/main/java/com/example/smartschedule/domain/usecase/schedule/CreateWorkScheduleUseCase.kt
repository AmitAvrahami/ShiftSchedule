package com.example.smartschedule.domain.usecase.schedule

import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.models.workschedule.WorkSchedule
import com.example.smartschedule.domain.repository.workschedule.WorkScheduleRepository
import java.time.LocalDate
import javax.inject.Inject

class CreateWorkScheduleUseCase @Inject constructor(
    private val workScheduleRepository: WorkScheduleRepository
) {
    suspend operator fun invoke(workSchedule: WorkSchedule): Result<Unit> =
        workScheduleRepository.createWorkSchedule(workSchedule)

}
