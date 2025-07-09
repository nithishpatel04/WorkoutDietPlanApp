package com.example.workoutdietplanapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.workoutdietplanapp.models.DietPlan
import com.example.workoutdietplanapp.models.WorkoutPlan
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

    // Home Screen Code
    val _isDietSelected = MutableStateFlow(false)
    val isDietSelected: StateFlow<Boolean> = _isDietSelected

    val _level = MutableStateFlow("Beginner") // Replace with user level later
    val level: StateFlow<String> = _level

    val _workouts = MutableStateFlow<List<WorkoutPlan>>(emptyList())
    val workouts: StateFlow<List<WorkoutPlan>> = _workouts

    val _dietPlan = MutableStateFlow<DietPlan?>(null)
    val dietPlan: StateFlow<DietPlan?> = _dietPlan

    init {
        loadPlans(_level.value)
    }

    fun toggleView() {
        _isDietSelected.value = !_isDietSelected.value
    }

    fun loadPlans(level: String) {
        _workouts.value = loadWorkouts(level)
        _dietPlan.value = loadDiet(level)
    }

    fun loadWorkouts(level: String): List<WorkoutPlan> {
        val base = when (level) {
            "Intermediate" -> 20
            "Advance" -> 35
            else -> 15
        }
        val reps = if (level == "Advance") 5 else 3
        val variations = if (level == "Advance") 5 else 3

        val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val exercises = listOf("Chest", "Back", "Shoulders", "Biceps", "Triceps", "Legs")

        return days.zip(exercises).map {
            WorkoutPlan(it.first, it.second, variations, reps, base)
        }
    }

    fun loadDiet(level: String): DietPlan {
        return when (level) {
            "Intermediate" -> DietPlan("Intermediate", 100, 10, listOf("2 boiled eggs", "1 banana", "No sugar drinks"))
            "Advance" -> DietPlan("Advance", 130, 12, listOf("Protein shake post workout", "Avoid processed food"))
            else -> DietPlan("Beginner", 80, 8, listOf("1 fruit a day", "Minimum 8 glasses of water"))
        }
    }

    fun Logout() {
        _user.value = User()
    }

    fun isLoggedIn(): Boolean = _user.value.isLoggedIn
}
