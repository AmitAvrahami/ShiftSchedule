package com.example.smartschedule.feature.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
) {
    // This assumes LoginViewModel exposes uiState, email, password, and event handlers.
    // You will need to update your LoginViewModel accordingly.
    // val uiState by viewModel.uiState.collectAsState()
    // val email by viewModel.email.collectAsState()
    // val password by viewModel.password.collectAsState()

    // For demonstration purposes, using local state here.
    // Replace with ViewModel state in your actual implementation.
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val isLoading = false // Replace with uiState.isLoading
    val error: String? = null // Replace with uiState.error

    LoginForm(
        email = email,
        password = password,
        onEmailChange = { email = it /* viewModel.onEmailChange(it) */ },
        onPasswordChange = { password = it /* viewModel.onPasswordChange(it) */ },
        onLoginClick = { /* viewModel.onLogin(onLoginSuccess) */ },
        onSignUpClick = onNavigateToSignUp,
        isLoading = isLoading,
        error = error
    )
}

@Composable
fun LoginForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    isLoading: Boolean,
    error: String?
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(0.5f))
            LoginHeader()
            Spacer(modifier = Modifier.height(32.dp))
            EmailField(value = email, onValueChange = onEmailChange, isEnabled = !isLoading)
            Spacer(modifier = Modifier.height(16.dp))
            PasswordField(value = password, onValueChange = onPasswordChange, isEnabled = !isLoading)
            Spacer(modifier = Modifier.height(8.dp))
            ForgotPasswordAction()
            Spacer(modifier = Modifier.height(24.dp))
            LoginButton(onClick = onLoginClick, isEnabled = !isLoading)
            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }
            error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.weight(1f))
            SignUpPrompt(onClick = onSignUpClick)
        }
    }
}

@Composable
private fun LoginHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "ברוכים הבאים!",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "התחברו כדי להמשיך",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EmailField(value: String, onValueChange: (String) -> Unit, isEnabled: Boolean) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("כתובת אימייל") },
        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        enabled = isEnabled
    )
}

@Composable
private fun PasswordField(value: String, onValueChange: (String) -> Unit, isEnabled: Boolean) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("סיסמה") },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (passwordVisible) "הסתר סיסמה" else "הצג סיסמה"
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description)
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        enabled = isEnabled
    )
}

@Composable
private fun ForgotPasswordAction() {
    TextButton(
        onClick = { /* TODO: Handle forgot password */ },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "שכחתי סיסמה?",
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary
        )
    }
}


@Composable
private fun LoginButton(onClick: () -> Unit, isEnabled: Boolean) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = isEnabled,
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = "התחברות", fontSize = 18.sp)
    }
}

@Composable
private fun SignUpPrompt(onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("אין לכם חשבון?")
        TextButton(onClick = onClick) {
            Text("הרשמה", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Preview(showBackground = true, name = "Login Form Preview")
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginForm(
            email = "israel@example.com",
            password = "password123",
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onSignUpClick = {},
            isLoading = false,
            error = null
        )
    }
}

@Preview(showBackground = true, name = "Login Form Loading")
@Composable
fun LoginScreenLoadingPreview() {
    MaterialTheme {
        LoginForm(
            email = "israel@example.com",
            password = "password123",
            onEmailChange = { },
            onPasswordChange = { },
            onLoginClick = { },
            onSignUpClick = {},
            isLoading = true,
            error = null
        )
    }
}

@Preview(showBackground = true, name = "Login Form Error")
@Composable
fun LoginScreenErrorPreview() {
    MaterialTheme {
        LoginForm(
            email = "israel@example.com",
            password = "wrongpassword",
            onEmailChange = { },
            onPasswordChange = { },
            onLoginClick = { },
            onSignUpClick = {},
            isLoading = false,
            error = "אימייל או סיסמה שגויים"
        )
    }
}
