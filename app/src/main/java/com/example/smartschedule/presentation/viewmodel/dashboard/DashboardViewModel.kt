package com.example.smartschedule.presentation.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartschedule.data.local.datastore.user_session.UserSessionManager
import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.models.user.User
import com.example.smartschedule.domain.models.user.roles.Roles
import com.example.smartschedule.domain.usecase.user.GetUserByIdUseCase
import kotlinx.coroutines.launch

open class DashboardViewModel(
    private val sessionManager: UserSessionManager,
    private val getUserByIdUseCase : GetUserByIdUseCase
) : ViewModel() {
    val role = sessionManager.userRoleFlow
    val userId = sessionManager.userIdFlow
    open fun logout() = viewModelScope.launch { sessionManager.clearSession() }
    open suspend fun getUserById(userId: String): Result<User?> {
        return getUserByIdUseCase.invoke(userId)
    }

    open fun refreshSession() = viewModelScope.launch { /*sessionManager.refreshSession()*/ }
    open fun navigateByRole(
        onManager: () -> Unit,
        onEmployee: () -> Unit
    ) = viewModelScope.launch {
        sessionManager.userRoleFlow.collect { role ->
            when (role) {
                Roles.MANAGER -> onManager()
                Roles.EMPLOYEE -> onEmployee()
                else -> {}
            }
        }
    }

}