package com.example.smartschedule.feature.smartSchedule.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.smartSchedule.Shift
import com.example.smartschedule.core.domain.model.smartSchedule.ShiftId
import com.example.smartschedule.core.domain.model.smartSchedule.enums.AssignmentStatus
import com.example.smartschedule.feature.smartSchedule.domain.repository.ScheduleRepository
import com.example.smartschedule.feature.smartSchedule.domain.rules.composite.ScheduleValidator
import com.example.smartschedule.feature.smartSchedule.ui.components.generateDateList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: ScheduleRepository,
    private val validator: ScheduleValidator
) : ViewModel() {

    private val _selectedShift = MutableStateFlow<Shift?>(null)

    /**
     * משתנה ה-State המרכזי.
     * ה-UI יאזין למשתנה הזה ויתעדכן אוטומטית בכל שינוי.
     */
    val uiState: StateFlow<ScheduleUiState> = combine(
        repository.getWorkSchedule("schedule_id_1"),
        repository.getEmployees(),
        repository.getConstraints(0, 0),
        repository.getWeeklyRules(),
        _selectedShift
    ) { schedule, employees, constraints, weeklyRules, selectedShift ->


        val validationResult = validator.validate(
            schedule = schedule,
            constraints = constraints,
            weeklyRules = weeklyRules
        )

        val allViolations = validationResult.errors + validationResult.warnings
        Log.d("ScheduleViewModel", " violations: ${allViolations}")
        val shiftsByDate = schedule.shifts.groupBy { it.date }

        val employeesById = employees.associateBy { it.id }

        val assignmentsByShiftId = schedule.assignments
            .filter { it.status == AssignmentStatus.ACTIVE }
            .groupBy { it.shiftId }

        val violationsByShiftId = allViolations.groupBy { it.relatedShiftId }

        val daysInRange = generateDateList(schedule.period.start, schedule.period.endInclusive)

        ScheduleUiState.Success(
            schedule = schedule,
            employees = employees,
            violations = allViolations,
            selectedShift = selectedShift ,
            shiftsByDate = shiftsByDate,
            employeesById = employeesById,
            assignmentsByShiftId = assignmentsByShiftId,
            violationsByShiftId = violationsByShiftId,
            daysInRange = daysInRange
        ) as ScheduleUiState
    }
        .catch { error ->
            emit(ScheduleUiState.Error(error.message ?: "שגיאה לא ידועה בטעינת הנתונים"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ScheduleUiState.Loading
        )


    fun toggleSelectedShift(shift: Shift) {
        val currentSelection = _selectedShift.value
        if (currentSelection == shift) {
            _selectedShift.value = null
        }else {
            _selectedShift.value = shift
        }
    }

    fun clearSelection() {
        _selectedShift.value = null
    }

    fun toggleEmployeeAssignment(shiftId : ShiftId , employeeId : EmployeeId) {
        val currentUiState = uiState.value
        if (currentUiState !is ScheduleUiState.Success) return

        val currentSchedule = currentUiState.schedule

        val isAssigned = currentSchedule.assignments.any {
            it.shiftId == shiftId && it.employeeId == employeeId && it.status == AssignmentStatus.ACTIVE
        }

        val updatedSchedule = if (isAssigned) {
            currentSchedule.unassignEmployee(shiftId, employeeId)
        } else {
            currentSchedule.assignEmployee(shiftId, employeeId)
        }

        viewModelScope.launch {
            repository.updateSchedule(updatedSchedule)
        }


    }
}