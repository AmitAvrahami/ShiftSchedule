package com.example.smartschedule.presentation.screens.auth.signup

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartschedule.domain.models.user.roles.Role
import com.example.smartschedule.domain.models.user.roles.Roles
import com.example.smartschedule.domain.usecase.auth.validation.ValidationField
import com.example.smartschedule.presentation.viewmodel.auth.SignUpViewModel
import com.example.smartschedule.ui.theme.SmartScheduleTheme

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateToLogin()
        }

    }
    SignUpScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun SignUpScreenContent(
    uiState: SignUpState,
    onEvent: (SignUpUiEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "SmartSchedule",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Create your account to start managing shifts",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // FULL NAME
        Text(
            text = "Full Name",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.fullName,
            onValueChange = { onEvent(SignUpUiEvent.FullNameChanged(it)) },
            label = { Text("Enter Your Full Name") },
            isError = uiState.errorField == ValidationField.FULL_NAME,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        if (uiState.errorField == ValidationField.FULL_NAME) {
            Text(
                text = uiState.error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 2.dp, bottom = 8.dp)
            )
        }

        // NATIONAL ID
        Text(
            text = "National ID",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.nationalId,
            onValueChange = { if (it.length <= 9) onEvent(SignUpUiEvent.NationalIdChanged(it)) },
            label = { Text("Enter your 9-digit ID") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = uiState.errorField == ValidationField.NATIONAL_ID,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        if (uiState.errorField == ValidationField.NATIONAL_ID) {
            Text(
                text = uiState.error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 2.dp, bottom = 8.dp)
            )
        }

        // ROLE
        Text(
            text = "Role",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray, MaterialTheme.shapes.small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,

        ) {
            RadioButton(
                selected = uiState.role == Roles.EMPLOYEE,
                onClick = {
                    onEvent(SignUpUiEvent.ToggleRole(Roles.EMPLOYEE))
                }
            )
            Text(
                text = "Employee",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 4.dp, end = 16.dp)
            )
            RadioButton(
                selected = uiState.role == Roles.MANAGER ,
                onClick = {
                    onEvent(SignUpUiEvent.ToggleRole(Roles.MANAGER))
                }
            )
            Text(
                text = "Manager",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 1.dp,)
            )
        }


        // EMAIL (optional)
        Text(
            text = "Email",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { onEvent(SignUpUiEvent.EmailChanged(it)) },
            label = { Text("Enter your email address") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = uiState.errorField == ValidationField.EMAIL,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        if (uiState.errorField == ValidationField.EMAIL) {
            Text(
                text = uiState.error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 2.dp, bottom = 8.dp)
            )
        }

        // PASSWORD
        Text(
            text = "Password",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { onEvent(SignUpUiEvent.PasswordChanged(it)) },
            label = { Text("create a password") },
            visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (uiState.isPasswordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { onEvent(SignUpUiEvent.TogglePasswordVisibility) }) {
                    Icon(imageVector = image, contentDescription = "Toggle password visibility")
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = uiState.errorField == ValidationField.PASSWORD,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        if (uiState.errorField == ValidationField.PASSWORD) {
            Text(
                text = uiState.error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 2.dp, bottom = 8.dp)
            )
        }

        // CONFIRM PASSWORD
        Text(
            text = "Confirm Password",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.confirmPassword,
            onValueChange = { onEvent(SignUpUiEvent.ConfirmPasswordChanged(it)) },
            label = { Text("Confirm your Password") },
            visualTransformation = if (uiState.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (uiState.isConfirmPasswordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { onEvent(SignUpUiEvent.ToggleConfirmPasswordVisibility) }) {
                    Icon(imageVector = image, contentDescription = "Toggle password visibility")
                }
            },
            isError = uiState.errorField == ValidationField.CONFIRM_PASSWORD,
            modifier = Modifier.fillMaxWidth()
        )
        if (uiState.errorField == ValidationField.CONFIRM_PASSWORD) {
            Text(
                text = uiState.error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 2.dp, bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TERMS CHECKBOX
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = uiState.agreeToTerms,
                onCheckedChange = { onEvent(SignUpUiEvent.AgreeToTermsChanged(it)) }
            )
            Text(
                text = "I agree to the Terms & Privacy Policy",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (uiState.errorField == ValidationField.TERMS) {
            Text(
                text = uiState.error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 2.dp, bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // BUTTON & FEEDBACK
        Button(
            onClick = { onEvent(SignUpUiEvent.SignUpClicked) },
            enabled = !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Sign Up")
            }
        }

        if (uiState.error != null && uiState.errorField == null) {
            Text(
                text = uiState.error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (uiState.isSuccess) {
            Text(
                text = "Account created successfully!",
                color = Color(0xFF2E7D32),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { onEvent(SignUpUiEvent.LoginClicked) }) {
            Text("Already have an account? Log in")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SmartScheduleTheme {
        SignUpScreenContent(uiState = SignUpState(), onEvent = {})
    }
}
