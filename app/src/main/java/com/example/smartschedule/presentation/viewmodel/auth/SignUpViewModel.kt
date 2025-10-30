package com.example.smartschedule.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.data.repository.AuthRepository
import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.usecase.auth.SignupUseCase
import com.example.smartschedule.domain.usecase.auth.validation.*
import com.example.smartschedule.presentation.screens.auth.signup.SignUpState
import com.example.smartschedule.presentation.screens.auth.signup.SignUpUiEvent
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val signUpUseCase: SignupUseCase,
    private val validateFullNameUseCase: ValidateFullNameUseCase,
    private val validateNationalIdUseCase: ValidateNationalIdUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateSignUpInputUseCase: ValidateSignUpInputUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpState())
    val uiState: StateFlow<SignUpState> = _uiState.asStateFlow()

    fun onEvent(event: SignUpUiEvent) {
        when (event) {
            is SignUpUiEvent.FullNameChanged -> {
                _uiState.update { it.copy(fullName = event.fullName) }
                val result = validateFullNameUseCase(event.fullName)
                handleValidationResult(result)
            }

            is SignUpUiEvent.NationalIdChanged -> {
                _uiState.update { it.copy(nationalId = event.nationalId) }
                val result = validateNationalIdUseCase(event.nationalId)
                handleValidationResult(result)
            }

            is SignUpUiEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email) }
                if (event.email.isNotBlank()) {
                    val result = validateEmailUseCase(event.email)
                    handleValidationResult(result)
                } else {
                    clearError()
                }
            }

            is SignUpUiEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password) }
                val result = validatePasswordUseCase(event.password)
                handleValidationResult(result)
            }

            is SignUpUiEvent.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = event.confirmPassword) }
            }

            is SignUpUiEvent.AgreeToTermsChanged -> {
                _uiState.update { it.copy(agreeToTerms = event.agreeToTerms) }
            }

            SignUpUiEvent.TogglePasswordVisibility ->
                _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }

            SignUpUiEvent.ToggleConfirmPasswordVisibility ->
                _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }

            SignUpUiEvent.SignUpClicked -> signUp()
            SignUpUiEvent.LoginClicked -> { /* Navigate to login */ }
        }
    }

    private fun handleValidationResult(result: ValidationResult) {
        if (!result.isSuccess) {
            _uiState.update {
                it.copy(
                    errorField = result.field,
                    error = result.errorMessage
                )
            }
        } else {
            clearError()
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(errorField = null, error = null) }
    }
    private fun signUp() {
        val state = _uiState.value

        val validation = validateSignUpInputUseCase(
            fullName = state.fullName,
            nationalId = state.nationalId,
            email = state.email.ifBlank { null },
            password = state.password,
            confirmPassword = state.confirmPassword,
            agreeToTerms = state.agreeToTerms
        )

        if (!validation.isSuccess) {
            _uiState.update {
                it.copy(
                    errorField = validation.field,
                    error = validation.errorMessage
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null, errorField = null) }

        viewModelScope.launch {
            signUpUseCase(
                nationalId = state.nationalId,
                name = state.fullName,
                email = state.email,
                password = state.password
            ).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update { it.copy(isLoading = true) }

                    is Result.Success -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            error = null,
                            errorField = null
                        )
                    }

                    is Result.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Sign up failed",
                            errorField = null
                        )
                    }
                }
            }
        }
    }

}
