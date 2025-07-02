package com.example.workoutdietplanapp.navigation

sealed class Route(val routeName: String) {
    object SignIn : Route("sign_in")
    object Registration : Route("registration")
    object Home : Route("home")
}
