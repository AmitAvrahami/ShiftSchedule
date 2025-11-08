package com.example.smartschedule.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Schedule () {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Schedule")
    }
}

@Composable
@Preview(showBackground = true)
fun SchedulePreview(){
    Schedule()
}