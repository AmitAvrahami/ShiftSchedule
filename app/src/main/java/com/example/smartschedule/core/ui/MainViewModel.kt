package com.example.smartschedule.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.core.data.datastore.UserPreferences
import com.example.smartschedule.core.domain.model.employees.enums.EmployeeRole
import com.example.smartschedule.core.ui.navigation.screens.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferences : UserPreferences
): ViewModel() {

    val userRole : StateFlow<EmployeeRole?> = userPreferences.userRole
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    val startDestination : StateFlow<String?> = userPreferences.userRole
        .map { role ->
            when(role){
                EmployeeRole.MANAGER -> Screen.ManagerDashboard.route
                EmployeeRole.EMPLOYEE -> Screen.EmployeeDashboard.route
                EmployeeRole.ADMIN -> Screen.AdminDashboard.route
                null -> Screen.Login.route
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}