package com.example.workoutdietplanapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.workoutdietplanapp.views.HomeScreen
import com.example.workoutdietplanapp.views.SignInScreen
import com.example.workoutdietplanapp.views.RegistrationFormScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "signin") {
        composable("signin") { SignInScreen(navController) }
        composable("registration") { RegistrationFormScreen(navController) }
        composable("home") { HomeScreen(navController) }
    }
}


