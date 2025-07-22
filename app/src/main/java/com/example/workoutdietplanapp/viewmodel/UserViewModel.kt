package com.example.workoutdietplanapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.workoutdietplanapp.firebase.FirebaseDatabaseHelper
import com.example.workoutdietplanapp.models.DietPlan
import com.example.workoutdietplanapp.models.WorkoutPlan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class User(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val age: Int = 0,
    val weight: Float = 0f,
    val goal: String = "",               // internal name is goal
    val subscriptionType: String = "",
    val gymPlan: Boolean = false,
    val dietPlan: Boolean = false,
    val isLoggedIn: Boolean = false
)

class UserViewModel : ViewModel() {

    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    private val _isDietSelected = MutableStateFlow(false)
    val isDietSelected: StateFlow<Boolean> = _isDietSelected

    private val _level = MutableStateFlow("Beginner")
    val level: StateFlow<String> = _level

    private val _workouts = MutableStateFlow<List<WorkoutPlan>>(emptyList())
    val workouts: StateFlow<List<WorkoutPlan>> = _workouts

    private val _dietPlan = MutableStateFlow<DietPlan?>(null)
    val dietPlan: StateFlow<DietPlan?> = _dietPlan

    init {
        loadPlans(_level.value)
    }

    fun login(email: String) {
        _user.value = _user.value.copy(email = email, isLoggedIn = true)
    }

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
            goal = motivation
        )
    }

    fun setUser(name: String, email: String, age: Int) {
        _user.value = _user.value.copy(
            name = name,
            email = email,
            age = age
        )
    }

    fun toggleView() {
        _isDietSelected.value = !_isDietSelected.value
    }

    fun loadPlans(level: String) {
        _workouts.value = loadWorkouts(level)
        _dietPlan.value = loadDiet(level)
    }

    private fun loadWorkouts(level: String): List<WorkoutPlan> {
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

    private fun loadDiet(level: String): DietPlan {
        return when (level) {
            "Intermediate" -> DietPlan("Intermediate", 100, 10, listOf("2 boiled eggs", "1 banana", "No sugar drinks"))
            "Advance" -> DietPlan("Advance", 130, 12, listOf("Protein shake post workout", "Avoid processed food"))
            else -> DietPlan("Beginner", 80, 8, listOf("1 fruit a day", "Minimum 8 glasses of water"))
        }
    }

    fun logout() {
        _user.value = User()
    }

    fun isLoggedIn(): Boolean = _user.value.isLoggedIn

    // Register user in Firebase Authentication + save user data to Firebase Realtime DB
    fun registerUser(
        email: String,
        password: String,
        name: String,
        age: Int,
        weight: Float,
        subscriptionType: String,
        gymPlan: Boolean,
        dietPlan: Boolean,
        motivation: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val newUser = User(
            email = email,
            password = password,
            name = name,
            age = age,
            weight = weight,
            subscriptionType = subscriptionType,
            gymPlan = gymPlan,
            dietPlan = dietPlan,
            goal = motivation,
            isLoggedIn = true
        )

        FirebaseDatabaseHelper.registerUserAuth(email, password) { authSuccess, authMessage ->
            if (authSuccess) {
                FirebaseDatabaseHelper.saveUserData(newUser) { saveSuccess ->
                    if (saveSuccess) {
                        _user.value = newUser
                        onResult(true, "User registered successfully")
                    } else {
                        onResult(false, "Failed to save user data")
                    }
                }
            } else {
                onResult(false, authMessage)
            }
        }
    }

    // Fetch user profile data from Firebase Realtime DB by email
    fun fetchUserData(email: String, onResult: (Boolean, String) -> Unit) {
        FirebaseDatabaseHelper.fetchUserData(email) { fetchedUser ->
            if (fetchedUser != null) {
                _user.value = fetchedUser.copy(isLoggedIn = true)
                onResult(true, "User data loaded")
            } else {
                onResult(false, "Failed to load user data")
            }
        }
    }

    // Update user profile locally after editing
    fun updateUserLocally(name: String, password: String, goal: String) {
        val current = _user.value
        _user.value = current.copy(name = name, password = password, goal = goal)
    }
}
