package com.example.smartschedule.feature.employees.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.core.data.datastore.UserPreferences
import com.example.smartschedule.core.domain.model.employees.Employee
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.Shift
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftAssignment
import com.example.smartschedule.core.domain.model.smartSchedule.WorkSchedule
import com.example.smartschedule.core.domain.model.smartSchedule.enums.AssignmentStatus
import com.example.smartschedule.feature.smartSchedule.domain.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class EmployeeDashboardViewModel @Inject constructor(
    private val repository: ScheduleRepository ,
    private val userPreferences: UserPreferences
) : ViewModel() {

    // נניח שיש לנו דרך לדעת מי המשתמש המחובר (לצורך הדוגמה נשתמש ב-ID 1 קבוע או נשלוף מה-Prefs)
    // באפליקציה אמיתית ה-ID מגיע מ-AuthRepository
    private val currentEmployeeId = "1"

    val uiState: StateFlow<EmployeeUiState> = combine(
        repository.getWorkSchedule("schedule_id_1"),
        repository.getEmployees()
    ) { schedule, allEmployees ->

        val employee = allEmployees.find { it.id.value == currentEmployeeId }
        val now = LocalDateTime.now()
        val today = LocalDate.now()

        // 1. חישוב המשמרת הבאה שלי
        val (myFutureShifts , nextShift , hoursUntil) = getNextShift(schedule , now)

        // 2. משמרות היום (מי עובד איתי / בחנות באופן כללי)
        val todayShiftsList = getTodayShifts(schedule , today , allEmployees)

        // 3. ימי השבוע לתצוגת לוח שנה
        val startOfWeek = today // אפשר לשפר לוגיקה לתחילת שבוע
        val weekDays = (0..6).map { startOfWeek.plusDays(it.toLong()) }

        EmployeeUiState(
            currentEmployee = employee,
            nextShift = nextShift,
            hoursToNextShift = hoursUntil,
            todayShifts = todayShiftsList,
            weekSchedule = weekDays,
            shiftsThisWeek = myFutureShifts, // או כל המשמרות השבוע
            isLoading = false
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EmployeeUiState())

    private fun getTodayShifts(
        schedule : WorkSchedule ,
        today : LocalDate? ,
        allEmployees : List<Employee>
    ) : List<ShiftDisplayModel> {
        val todayShiftsList = schedule.shifts
            .filter { it.date == today }
            .sortedBy { it.startTime }
            .map { shift ->
                // מי עובד במשמרת הזו?
                val workersInShift = schedule.assignments
                    .filter { it.shiftId == shift.id }
                    .mapNotNull { assignment -> allEmployees.find { it.id == assignment.employeeId }?.fullName }

                ShiftDisplayModel(
                    shift ,
                    workersInShift
                )
            }
        return todayShiftsList
    }


    private fun getNextShift(
    schedule : WorkSchedule ,
    now : LocalDateTime?
) : Triple<List<Shift> , Shift? , Long?> {
    val myAssignments = schedule.getAssignmentsForEmployee(EmployeeId(currentEmployeeId))

    val myFutureShifts = myAssignments.mapNotNull { assignment ->
        schedule.shifts.find { it.id == assignment.shiftId }
    }.filter { shift ->
        val shiftStart = LocalDateTime.of(
            shift.date ,
            shift.startTime
        )
        shiftStart.isAfter(now)
    }.sortedBy {
        LocalDateTime.of(
            it.date ,
            it.startTime
        )
    }

    val nextShift = myFutureShifts.firstOrNull()
    val hoursUntil = nextShift?.let {
        ChronoUnit.HOURS.between(
            now ,
            LocalDateTime.of(
                it.date ,
                it.startTime
            )
        )
    }
    return Triple(
        myFutureShifts ,
        nextShift ,
        hoursUntil
    )
}
    }