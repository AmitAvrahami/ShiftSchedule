package com.example.smartschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartschedule.feature.smartSchedule.ui.ScheduleScreen
import com.example.smartschedule.ui.theme.SmartScheduleTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartScheduleTheme {
                ScheduleScreen()
            }
        }
    }
}

//        // 1. צור את אובייקט הדמה
//        val dummyWorkSchedule = WorkScheduleMapper.createDummyWorkSchedule()
//
//// 2. המר אותו למפה
//        val workScheduleMap = WorkScheduleMapper.toMap(dummyWorkSchedule)
//
// val firestore = FirebaseFirestore.getInstance()
// firestore.collection("workSchedules").add(workScheduleMap)
//     .addOnSuccessListener { documentReference ->
//         Log.d("Firebase", "DocumentSnapshot added with ID: ${documentReference.id}")
//     }
//     .addOnFailureListener { e ->
//         Log.w("Firebase", "Error adding document", e)
//     }
//
//    }

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
