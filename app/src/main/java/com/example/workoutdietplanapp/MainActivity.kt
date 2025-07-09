package com.example.workoutdietplanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.workoutdietplanapp.navigation.AppNavGraph
import com.example.workoutdietplanapp.ui.theme.WorkoutDietPlanAppTheme
import com.example.workoutdietplanapp.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutDietPlanAppTheme {
                val navController = rememberNavController()
                val userViewModel: UserViewModel = viewModel()
                AppNavGraph(navController = navController, userViewModel = userViewModel )
            }
        }
    }
}