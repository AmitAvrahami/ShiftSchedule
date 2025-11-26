package com.example.smartschedule.feature.auth.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.core.data.datastore.UserPreferences
import com.example.smartschedule.core.domain.model.employees.enums.EmployeeRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    fun onLoginAsManager(onSuccess: () -> Unit) {
        viewModelScope.launch {
            userPreferences.saveUserRole(EmployeeRole.MANAGER)
            onSuccess()
        }
    }

    fun onLoginAsEmployee(onSuccess: () -> Unit) {
        viewModelScope.launch {
            userPreferences.saveUserRole(EmployeeRole.EMPLOYEE)
            onSuccess()
        }
    }
}