package com.example.smartschedule.presentation.screens.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartschedule.R
import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.presentation.viewmodel.auth.LoginViewModel

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit
) {
    val loginResult by loginViewModel.loginResult.collectAsState()
    val uiState by loginViewModel.uiState.collectAsState()

    // Handle successful login navigation
    LaunchedEffect(loginResult) {
        if (loginResult is Result.Success) {
            onLoginSuccess()
        }
    }

    LoginContent(
        uiState = uiState,
        loginResult = loginResult,
        onEvent = loginViewModel::onEvent,
        onSignUpClick = onSignUpClick
    )
}

@Composable
fun LoginContent(
    uiState: LoginState,
    loginResult: Result<*>?,
    onEvent: (LoginUiEvent) -> Unit,
    onSignUpClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with your logo
                contentDescription = "Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Welcome Back!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Sign in to continue your journey.", color = Color.Gray)

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { onEvent(LoginUiEvent.EmailChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email Address") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
                isError = uiState.error != null,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = { onEvent(LoginUiEvent.PasswordChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
                isError = uiState.error != null,
                trailingIcon = {
                    IconButton(onClick = { onEvent(LoginUiEvent.TogglePasswordVisibility) }) {
                        Icon(
                            imageVector = if (uiState.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Password Visibility"
                        )
                    }
                },
                visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onEvent(LoginUiEvent.OnForgotPasswordClicked) }) {
                    Text("Forgot password?")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onEvent(LoginUiEvent.OnLoginClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = loginResult !is Result.Loading // Disable button while loading
            ) {
                Text("Login", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text("Don't have an account?")
                TextButton(onClick = { onSignUpClick() }) {
                    Text("Sign up")
                }
            }

            // Handle login state changes
            Box(modifier = Modifier.height(24.dp), contentAlignment = Alignment.Center) {
                if (loginResult is Result.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
                uiState.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginContent(
        uiState = LoginState("you@example.com", "password"),
        loginResult = null,
        onSignUpClick = {},
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenLoadingPreview() {
    LoginContent(
        uiState = LoginState(),
        loginResult = Result.Loading,
        onSignUpClick = {},
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenErrorPreview() {
    LoginContent(
        uiState = LoginState(error = "Invalid credentials. Please try again."),
        loginResult = Result.Error(Exception()),
        onSignUpClick = {},
        onEvent = {}
    )
}


