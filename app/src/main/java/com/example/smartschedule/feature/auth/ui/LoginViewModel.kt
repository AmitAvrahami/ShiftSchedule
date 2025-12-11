package com.example.smartschedule.feature.auth.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.core.data.datastore.UserPreferences
import com.example.smartschedule.core.domain.model.employees.enums.EmployeeRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: MutableStateFlow<LoginUiState> = _uiState



    fun onEmailChange(email: String) {
        // Handle email change
    }

    fun onPasswordChange(password: String) {
        // Handle password change
    }

    fun onLoginClicked(onSuccess: () -> Unit) {
        // Handle login click
    }

    fun onSignUpClicked(onSuccess: () -> Unit) {
        // Handle sign up click
    }
}



data class LoginUiState(
    val email : String = "",
    val password : String = "",
    val isLoading : Boolean = false,
    val error : String? = null
)