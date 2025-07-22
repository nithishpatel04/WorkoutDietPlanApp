package com.example.workoutdietplanapp.utils

import android.content.Context
import android.content.SharedPreferences

class UserPreferences (context: Context) {
    // Define keys for shared preferences
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // fun to set first time launch flag
    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        sharedPreferences.edit().putBoolean("isFirstTime", isFirstTime).apply()
    }

    // fun to check if it's the first time launch
    fun isFirstTimeLaunch(): Boolean = sharedPreferences.getBoolean("isFirstTime", true)

    // fun to save login credentials
    fun saveLoginCredentials(email: String, password: String) {
        sharedPreferences.edit().putString("email", email).putString("password", password).apply()
    }

    // fun to retrieve login credentials
    fun getUserEmail(): String? = sharedPreferences.getString("email", "") ?: ""
    fun getUserPassword(): String? = sharedPreferences.getString("password", "") ?: ""

    // fun to set remember me flag
    fun setRememberMe(rememberMe: Boolean) {
        sharedPreferences.edit().putBoolean("rememberMe", rememberMe).apply()
    }

    // fun to check if remember me is enabled
    fun isRememberMeEnabled(): Boolean {
        return sharedPreferences.getBoolean("rememberMe", false)
    }

    // fun to clear login credentials on logout
    fun clearLoginCredentials() {
        sharedPreferences.edit().clear().apply()
    }
}