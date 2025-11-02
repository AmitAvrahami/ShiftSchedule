package com.example.smartschedule.presentation.viewmodel.dashboard

import com.example.smartschedule.data.local.datastore.user_session.UserSessionManager
import com.example.smartschedule.domain.models.user.User
import com.example.smartschedule.presentation.screens.dashboards.employee.DashboardAction
import kotlinx.coroutines.Job
import javax.inject.Inject
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.usecase.user.GetUserByIdUseCase
import com.example.smartschedule.presentation.screens.dashboards.employee.EmployeeDashboardState
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.job
import kotlinx.coroutines.launch


class EmployeeDashboardViewModel @Inject constructor(
    private val sessionManager: UserSessionManager,
    private val getUserByIdUseCase : GetUserByIdUseCase
) : DashboardViewModel(sessionManager, getUserByIdUseCase) {

    var state by mutableStateOf(EmployeeDashboardState())
        private set




    fun onDashboardAction(action: DashboardAction){}

    fun loadEmployeeDetails() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            try {
                val userId = sessionManager.userIdFlow.firstOrNull()

                if (userId.isNullOrEmpty()) {
                    state = state.copy(
                        isLoading = false,
                        errorMessage = "No user ID found in session"
                    )
                    return@launch
                }

                when (val result = getUserById(userId)) {
                    else -> handleResult(result,
                        onSuccess = { user ->
                            if (user != null) {
                                state = state.copy(
                                    user = user,
                                    employeeName = user.fullName,
                                    employeeRole = user.role.getRole().displayName
                                )
                            }
                        },
                        onError = { msg ->
                            state = state.copy(errorMessage = msg)
                        }
                    )
                }

            } catch (e: Exception) {
                state = state.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unexpected error"
                )
            }
        }
    }


    fun loadNextShift(){}
    fun loadTodayShifts(){}
    fun loadWeeklyShifts(){}
    fun loadManagerMessages(){}
    fun markMessageAsRead(messageId: String){}
    fun onQuickActionSelected(action: Any){}
    fun requestShiftSwap(shiftId: String){}
    fun viewFullSchedule(){}
    fun handleError(){}

    override fun logout() : Job {
        return super.logout()
    }
    override fun refreshSession() : Job {
        return super.refreshSession()
    }
    override fun navigateByRole(
        onManager : () -> Unit ,
        onEmployee : () -> Unit
    ) : Job {
        return super.navigateByRole(onManager , onEmployee)
    }

    private fun <T> handleResult(
        result: Result<T>,
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit = { message ->
            state = state.copy(
                isLoading = false,
                errorMessage = message
            )
        }
    ) {
        when (result) {
            is Result.Success -> {
                state = state.copy(isLoading = false, errorMessage = null)
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