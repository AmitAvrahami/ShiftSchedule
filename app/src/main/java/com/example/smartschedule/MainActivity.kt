package com.example.smartschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.smartschedule.core.ui.MainAppScreen
import com.example.smartschedule.core.ui.MainViewModel
import com.example.smartschedule.core.ui.navigation.AppNavGraph
import com.example.smartschedule.ui.theme.SmartScheduleTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            viewModel.startDestination.value == null
        }

        super.onCreate(savedInstanceState)
        setContent {
            SmartScheduleTheme {
                val startDest by viewModel.startDestination.collectAsState()
                val userRole by viewModel.userRole.collectAsStateWithLifecycle()

                if (startDest != null) {
                    MainAppScreen(
                        startDestination = startDest !!,
                        userRole = userRole
                    )
                }
            }
        }
    }
}


@Composable
fun Greeting(name : String , modifier : Modifier = Modifier) {
    Text(
        text = "Hello $name!" ,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmartScheduleTheme {
        Greeting("Android")
    }
}
