package com.example.workoutdietplanapp.navigation

sealed class Route(val routeName: String) {
    object Welcome : Route("welcome")
    object SignIn : Route("signin")
    object Registration : Route("registration")
    object Home : Route("home")
    object Profile : Route("profile")
}
