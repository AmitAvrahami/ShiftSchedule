package com.example.smartschedule.presentation.viewmodel.dashboard

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.data.local.datastore.user_session.UserSessionManager
import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.models.workschedule.WorkSchedule
import com.example.smartschedule.domain.repository.workschedule.WorkScheduleRepository
import com.example.smartschedule.domain.usecase.assigment.GetNextAssignmentUseCase
import com.example.smartschedule.domain.usecase.user.GetUserByIdUseCase
import com.example.smartschedule.presentation.navigation.Screen
import com.example.smartschedule.presentation.screens.dashboards.employee.DashboardAction
import com.example.smartschedule.presentation.screens.dashboards.employee.EmployeeDashboardState
import com.example.smartschedule.presentation.screens.dashboards.employee.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EmployeeDashboardViewModel @Inject constructor(
    private val sessionManager: UserSessionManager,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val workScheduleRepository: WorkScheduleRepository
) : DashboardViewModel(sessionManager, getUserByIdUseCase) {

    private val TAG = "EmployeeDashboardVM"
    var state by mutableStateOf(EmployeeDashboardState())
        private set

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()




    fun onDashboardAction(action : DashboardAction) {
        when (action) {
            is DashboardAction.ContactManager -> onContactManagerClicked()
            is DashboardAction.LoadData -> loadData()
            is DashboardAction.RequestShiftSwap -> onRequestShiftSwapClicked()
           is DashboardAction.ViewSchedule -> onViewScheduleClicked()
        }

    }

    private fun onViewScheduleClicked() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.Navigate(Screen.ViewSchedule.route))
        }
    }

    private fun onRequestShiftSwapClicked() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.Navigate(Screen.RequestShiftSwap.route))
        }
    }

    private fun onContactManagerClicked() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.Navigate(Screen.ContactManager.route))

        }
    }

    fun loadData() {
        Log.d(TAG , "▶️ loadData() started")

        viewModelScope.launch {
            state = state.copy(isLoading = true)

            try {
                loadEmployeeDetails()
            } catch (e : Exception) {
                setError(e.message ?: "שגיאה בלתי צפויה בטעינת הנתונים")
            } finally {
                state = state.copy(isLoading = false)
                Log.d(TAG , "🏁 loadData() finished")
            }
        }
    }

    fun loadEmployeeDetails() {
        viewModelScope.launch {
            Log.d(TAG , "▶️ loadEmployeeDetails() started")
            state = state.copy(isLoading = true)

            try {
                val userId = sessionManager.userIdFlow.firstOrNull()
                Log.d(TAG , "userId from session: $userId")

                if (userId.isNullOrEmpty()) {
                    setError("לא נמצא מזהה משתמש במערכת")
                    return@launch
                }

                val result = getUserById(userId)
                handleResult(
                    result ,
                    onSuccess = { user ->
                        if (user != null) {
                            Log.d(TAG , "✅ User loaded: ${user.fullName}")

                            state = state.copy(
                                user = user ,
                                isLoading = false ,
                                errorMessage = null
                            )

                            loadSchedule {
                                loadNextShift()
                                loadTodayAssignments()
                                loadWeeklyShifts()
                                loadManagerMessages()
                                dayAndDateMap()
                                employeeWorkingDatesMap()
                                nextShiftPeriod()
                            }
                        } else {
                            setError("משתמש לא נמצא")
                        }
                    } ,
                    onError = { msg -> setError(msg) }
                )

            } catch (e : Exception) {
                setError(e.message ?: "שגיאה בלתי צפויה בטעינת המשתמש")
            } finally {
                Log.d(TAG , "🏁 loadEmployeeDetails() finished")
            }
        }
    }

    fun loadSchedule(onLoaded : (WorkSchedule) -> Unit = {}) {
        Log.d(TAG , "▶️ loadSchedule() started")

        viewModelScope.launch {
            state.user?.id ?: run {
                setError("משתמש לא מחובר")
                return@launch
            }

            val existing = state.schedule
            if (existing != null) {
                Log.d(TAG , "✅ Schedule already loaded")
                onLoaded(existing)
                return@launch
            }

            try {
                state = state.copy(isLoading = true)

                when (val result = workScheduleRepository.getActiveWorkSchedule()) {
                    is Result.Success -> {
                        val loaded = result.data
                        if (loaded != null) {
                            Log.d(TAG , "✅ Schedule loaded successfully: ${loaded.title}")
                            state = state.copy(schedule = loaded , isLoading = false)
                            onLoaded(loaded)
                        } else {
                            setError("לא נמצא סידור עבודה פעיל")
                        }
                    }

                    is Result.Error -> setError(
                        result.exception.message ?: "שגיאה בטעינת סידור העבודה"
                    )

                    Result.Loading -> state = state.copy(isLoading = true)
                }

            } catch (e : Exception) {
                setError(e.message ?: "שגיאה בלתי צפויה בטעינת סידור העבודה")
            } finally {
                Log.d(TAG , "🏁 loadSchedule() finished")
            }
        }
    }

    fun loadNextShift() {
        loadSchedule { schedule ->
            val userId = state.user?.id ?: return@loadSchedule
            val next = schedule.getNextAssignment(userId)
            if (next != null) {
                state = state.copy(nextAssignment = next)
                Log.d(TAG , "✅ Next shift loaded: ${next.shift.startDate}")
            } else {
                state = state.copy(nextAssignment = null)
                Log.d(TAG , "✅ No next shift found")
            }
        }
    }

    fun loadTodayAssignments() {
        Log.d(TAG , "▶️ loadTodayAssignments() started")
        loadSchedule { schedule ->
            viewModelScope.launch {
                try {
                    val today = LocalDate.now()
                    val todayAssignments = schedule.assignments.filter {
                        it.shift.startDate.toLocalDate().isEqual(today)
                    }

                    if (todayAssignments.isNotEmpty()) {
                        Log.d(TAG , "✅ Found ${todayAssignments.size} assignments for today")
                        state = state.copy(todayAssignments = todayAssignments , isLoading = false)
                    } else {
                        state = state.copy(todayAssignments = emptyList() , isLoading = false)
                        Log.d(TAG , "✅ No assignments found for today")
                    }
                } catch (e : Exception) {
                    setError(e.message ?: "שגיאה בטעינת משמרות היום")
                } finally {
                    Log.d(TAG , "🏁 loadTodayAssignments() finished")
                }
            }
        }
    }

    fun loadWeeklyShifts() {
        Log.d(TAG , "▶️ loadWeeklyShifts() started")
        loadSchedule { schedule ->
            viewModelScope.launch {
                try {
                    val weeklyAssignments = schedule.getShiftAssignmentsForWeek()
                    if (weeklyAssignments.isNotEmpty()) {
                        state = state.copy(weeklyShifts = weeklyAssignments , isLoading = false)
                        Log.d(TAG , "✅ Weekly shifts loaded: ${weeklyAssignments.size}")
                    } else {
                        state = state.copy(weeklyShifts = emptyList() , isLoading = false)
                        Log.d(TAG , "✅ No weekly shifts found")
                    }
                } catch (e : Exception) {
                    setError(e.message ?: "שגיאה בטעינת משמרות השבוע")
                } finally {
                    Log.d(TAG , "🏁 loadWeeklyShifts() finished")
                }
            }
        }
    }

    fun loadManagerMessages() {
        Log.d(TAG , "▶️ loadManagerMessages() started")
        viewModelScope.launch {
            try {
                val dummy = listOf(
                    "נא להגיע 15 דקות מוקדם למשמרת היום 🕒" ,
                    "ישיבת צוות ביום שני ב־09:00" ,
                    "הסידור המעודכן לחגים פורסם באפליקציה 📅"
                )
                state = state.copy(managerMessages = dummy , isLoading = false)
                Log.d(TAG , "✅ Loaded ${dummy.size} manager messages")
            } catch (e : Exception) {
                setError(e.message ?: "שגיאה בטעינת הודעות המנהל")
            } finally {
                Log.d(TAG , "🏁 loadManagerMessages() finished")
            }
        }
    }

    fun dayAndDateMap(){
        Log.d(TAG, "▶️ dayAndDateMap() started")
        val dayDateMap = mutableMapOf<String, String>()
        val dates = state.schedule?.getDates()
        val days = state.schedule?.getWeekDays()
        Log.d(TAG, "Dates from schedule: $dates")
        Log.d(TAG, "Days from schedule: $days")

        dayDateMap.run {
            if (days != null && dates != null && days.size == dates.size) {
                days.forEachIndexed { index, day ->
                    this[day] = dates[index]
                }
            }
        }
        state = state.copy(dayDateMap = dayDateMap)
        Log.d(TAG, "✅ Day-Date map created: $dayDateMap")
    }

    fun nextShiftPeriod() {
        Log.d(TAG, "▶️ nextShiftPeriod() started")

        val nextShiftDate = state.nextAssignment?.shift?.startDate
        if (nextShiftDate == null) {
            Log.w(TAG, "⚠️ No upcoming shift found")
            return
        }

        val now = LocalDateTime.now()

        if (nextShiftDate.isBefore(now)) {
            Log.w(TAG, "⚠️ Next shift has already started or passed")
            return
        }

        val duration = java.time.Duration.between(now, nextShiftDate)
        val days = duration.toDays()
        val hours = duration.toHours() % 24
        val minutes = duration.toMinutes() % 60

        val (value, unit) = when {
            days > 0 -> days to "days"
            hours > 0 -> hours to "hours"
            else -> minutes to "minutes"
        }

        Log.d(TAG, "🕒 Next shift in $value $unit")

        state = state.copy(
            nextShiftCountdown = Pair(value.toString(), unit)
        )
    }



    fun employeeWorkingDatesMap(){
        Log.d(TAG, "▶️ employeeWorkingDatesMap() started")
        val workingDatesMap = mutableMapOf<String, Boolean>()
        val userId = state.user?.id
        Log.d(TAG, "employeeWorkingDatesMap() Current user ID: $userId")
        state.schedule?.assignments?.forEach { assignment ->
            val shiftDate = assignment.shift.startDate.dayOfMonth.toString()
            val isWorking = assignment.assignedEmployees.any { it.trim() == userId?.trim()  }
            Log.d(TAG,"employeeWorkingDatesMap() assignment: ${assignment.assignedEmployees}")
            workingDatesMap[shiftDate] = isWorking
            Log.d(TAG, "employeeWorkingDatesMap() Processing assignment for date $shiftDate. Is user working? $isWorking")
        }
        state = state.copy(workingDatesMap = workingDatesMap)
        Log.d(TAG, "✅employeeWorkingDatesMap() Employee working dates map created: $workingDatesMap")
    }


    private fun setError(message : String) {
        state = state.copy(isLoading = false , errorMessage = message)
        Log.e(TAG , "❌ $message")
    }


    fun markMessageAsRead(messageId : String) {}
    fun onQuickActionSelected(action : Any) {}
    fun requestShiftSwap(shiftId : String) {}
    fun viewFullSchedule() {}
    fun handleError() {}

    private fun <T> handleResult(
        result : Result<T> ,
        onSuccess : (T) -> Unit ,
        onError : (String) -> Unit = { message ->
            state = state.copy(
                isLoading = false ,
                errorMessage = message
            )
        }
    ) {
        when (result) {
            is Result.Success -> {
                state = state.copy(isLoading = false , errorMessage = null)
                onSuccess(result.data)
            }

            is Result.Error -> {
                val errorMsg = result.exception.message ?: "An unexpected error occurred"
                onError(errorMsg)
            }

            Result.Loading -> {
                state = state.copy(isLoading = true)
            }
        }
    }
}






