package com.example.workoutdietplanapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.workoutdietplanapp.views.*
import com.example.workoutdietplanapp.viewmodel.UserViewModel

@Composable
fun AppNavGraph(navController: NavHostController, userViewModel: UserViewModel) {
    NavHost(navController = navController, startDestination = Route.SignIn.routeName) {
        composable(Route.SignIn.routeName) { SignInScreen(navController, userViewModel) }
        composable(Route.Registration.routeName) {
            RegistrationForm(
                navController = navController,
                userViewModel = userViewModel,
                modifier = Modifier
            )
        }
        composable(Route.Home.routeName) { HomeScreen(navController, userViewModel) }
        composable(Route.Profile.routeName) { ProfileScreen(navController, userViewModel) }
    }
}