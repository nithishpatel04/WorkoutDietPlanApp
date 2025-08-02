package com.example.workoutdietplanapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdietplanapp.firebase.FirebaseDatabaseHelper
import com.example.workoutdietplanapp.models.DietPlan
import com.example.workoutdietplanapp.models.WorkoutPlan
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// User data class

data class User(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val age: Int = 0,
    val height: Float = 170f,
    val weight: Float = 70f,
    val goal: String = "",
    val subscriptionType: String = "",
    val gymPlan: Boolean = false,
    val dietPlan: Boolean = false,
    val isLoggedIn: Boolean = false
)

class UserViewModel : ViewModel() {

    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    val heightCm: MutableStateFlow<Float> = MutableStateFlow(_user.value.height)
    val weightKg: MutableStateFlow<Float> = MutableStateFlow(_user.value.weight)
    val goal: MutableStateFlow<String> = MutableStateFlow(_user.value.goal)
    val subscriptionType: MutableStateFlow<String> = MutableStateFlow(_user.value.subscriptionType)
    val gymPlan: MutableStateFlow<Boolean> = MutableStateFlow(_user.value.gymPlan)
    val dietPlanSelected: MutableStateFlow<Boolean> = MutableStateFlow(_user.value.dietPlan)
    val isDietSelected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val level: MutableStateFlow<String> = MutableStateFlow("Beginner")

    private val _workouts = MutableStateFlow<List<WorkoutPlan>>(emptyList())
    val workouts: StateFlow<List<WorkoutPlan>> = _workouts

    private val _dietPlan = MutableStateFlow<DietPlan?>(null)
    val dietPlan: StateFlow<DietPlan?> = _dietPlan

    init {
        viewModelScope.launch {
            _user.collect { user ->
                heightCm.value = user.height
                weightKg.value = user.weight
                goal.value = user.goal
                subscriptionType.value = user.subscriptionType
                gymPlan.value = user.gymPlan
                dietPlanSelected.value = user.dietPlan
            }
        }
        loadPlans(level.value)
    }

    fun login(email: String) {
        _user.value = _user.value.copy(email = email, isLoggedIn = true)
    }

    fun updateProfile(
        email: String,
        oldPassword: String,
        newPassword: String,
        name: String,
        age: Int,
        height: Float,
        weight: Float,
        motivation: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val currentUser = FirebaseAuth.getInstance().currentUser ?: throw Exception("User not authenticated")
                val credential = EmailAuthProvider.getCredential(email, oldPassword)
                currentUser.reauthenticate(credential).await()
                currentUser.updatePassword(newPassword).await()

                val updatedUser = _user.value.copy(
                    email = email,
                    password = newPassword,
                    name = name,
                    age = age,
                    height = height,
                    weight = weight,
                    goal = motivation
                )

                val saveSuccess = FirebaseDatabaseHelper.saveUserDataSuspend(updatedUser)
                if (!saveSuccess) throw Exception("Failed to save profile data")

                _user.value = updatedUser
                onResult(true, "Profile updated successfully")
            } catch (e: Exception) {
                onResult(false, e.message ?: "Profile update failed")
            }
        }
    }

    fun setUser(
        name: String,
        email: String,
        age: Int,
        height: Float,
        weight: Float,
        subscriptionType: String = "",
        gymPlan: Boolean = false,
        dietPlan: Boolean = false,
        goal: String = ""
    ) {
        _user.value = _user.value.copy(
            name = name,
            email = email,
            age = age,
            height = height,
            weight = weight,
            subscriptionType = subscriptionType,
            gymPlan = gymPlan,
            dietPlan = dietPlan,
            goal = goal
        )
    }


    fun updateHeight(height: Float) {
        _user.value = _user.value.copy(height = height)
    }

    fun updateWeight(weight: Float) {
        _user.value = _user.value.copy(weight = weight)
    }

    fun toggleView() {
        isDietSelected.value = !isDietSelected.value
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

    fun registerUser(
        email: String,
        password: String,
        name: String,
        age: Int,
        height: Float,
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
            height = height,
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
}
