package com.example.workoutdietplanapp.models

data class WorkoutPlan (
    val day: String,
    val workout: String,
    val variations: Int,
    val reps: Int,
    val maxWeightKg: Int
)