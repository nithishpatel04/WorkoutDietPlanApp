package com.example.workoutdietplanapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Updated data class with new fields
data class User(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val age: Int = 0,
    val weight: Float = 0f,
    val goal: String = "", // This will now be treated as "motivation"
    val subscriptionType: String = "",
    val gymPlan: Boolean = false,
    val dietPlan: Boolean = false,
    val isLoggedIn: Boolean = false
)

class UserViewModel : ViewModel() {
    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    fun login(email: String) {
        _user.value = _user.value.copy(email = email, isLoggedIn = true)
    }

    // Updated updateProfile to include new fields
    fun updateProfile(
        email: String,
        password: String,
        name: String,
        age: Int,
        weight: Float,
        subscriptionType: String,
        gymPlan: Boolean,
        dietPlan: Boolean,
        motivation: String
    ) {
        _user.value = _user.value.copy(
            email = email,
            password = password,
            name = name,
            age = age,
            weight = weight,
            subscriptionType = subscriptionType,
            gymPlan = gymPlan,
            dietPlan = dietPlan,
            goal = motivation // 'goal' in User is being used for motivation
        )
    }

    fun Logout() {
        _user.value = User()
    }

    fun isLoggedIn(): Boolean = _user.value.isLoggedIn
}
