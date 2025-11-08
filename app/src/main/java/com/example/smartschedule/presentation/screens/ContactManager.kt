package com.example.smartschedule.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartschedule.presentation.navigation.Screen

@Composable
fun ContactManager () {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "ContactManager")
    }
}

@Composable
@Preview(showBackground = true)
fun ContactManagerPreview(){
    ContactManager()
}