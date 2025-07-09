package com.example.workoutdietplanapp.models

data class DietPlan(
    val level: String,
    val proteinGrams: Int,
    val waterGlasses: Int,
    val extraTips: List<String>
)