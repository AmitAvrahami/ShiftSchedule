package com.example.smartschedule.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.data.repository.auth.AuthRepository
import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.models.user.User
import com.example.smartschedule.domain.usecase.auth.LoginUseCase
import com.example.smartschedule.domain.usecase.auth.validation.ValidateEmailUseCase
import com.example.smartschedule.domain.usecase.auth.validation.ValidatePasswordUseCase
import com.example.smartschedule.presentation.screens.auth.login.LoginState
import com.example.smartschedule.presentation.screens.auth.login.LoginUiEvent
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val emailValidator: ValidateEmailUseCase,
    private val passwordValidator: ValidatePasswordUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginResult = MutableStateFlow<Result<User?>?>(null)
    val loginResult: StateFlow<Result<User?>?> = _loginResult.asStateFlow()

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onEvent(event: LoginUiEvent) {
        println("LoginViewModel: onEvent($event)")
        when (event) {
            is LoginUiEvent.EmailChanged -> onEmailChanged(event.email)
            is LoginUiEvent.PasswordChanged ->  onPasswordChanged(event.password)
            is LoginUiEvent.OnLoginClicked -> login()
            is LoginUiEvent.OnSignUpClicked -> println("Navigate to Sign Up") // Handle navigation
            is LoginUiEvent.OnForgotPasswordClicked -> println("Navigate to Forgot Password") // Handle navigation
            is LoginUiEvent.TogglePasswordVisibility -> _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
        }
    }

    fun onEmailChanged(email: String) {
        println("LoginViewModel: onEmailChanged(email: $email)")
        _uiState.update { it.copy(email = email) }
        val result = emailValidator.invoke(email)
        result.errorMessage?.let { e ->
            _uiState.update { it.copy(error =  e) }
        }
        if (result.isSuccess) {
            _uiState.update { it.copy(error = null) }
        }
    }

    fun onPasswordChanged(password: String) {
        println("LoginViewModel: onPasswordChanged(password: $password)")
        _uiState.update {
            it.copy(password = password)
        }
        val result = passwordValidator.invoke(password)
        result.errorMessage?.let { e ->
            _uiState.update { it.copy(error = e) }
        }
        if (result.isSuccess) {
            _uiState.update {
                it.copy(error = null)

            }
        }
    }


    private fun login() {
        val email = _uiState.value.email
        val password = _uiState.value.password

        viewModelScope.launch {
            loginUseCase(email, password).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update { it.copy(isLoading = true, error = null) }

                    is Result.Success -> {
                        _uiState.update { it.copy(isLoading = false, error = null, isSuccess = true) }
                        _loginResult.value = result
                    }

                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false, error = result.exception.message) }
                        _loginResult.value = result
                    }
                }
            }
        }
    }


    fun logout() {
        println("LoginViewModel: logout()")
        viewModelScope.launch {
            authRepository.logout()
            _loginResult.value = null // Reset state after logout
        }
    }
}
