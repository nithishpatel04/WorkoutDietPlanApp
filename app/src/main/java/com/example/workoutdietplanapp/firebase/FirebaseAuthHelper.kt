package com.example.workoutdietplanapp.firebase

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthHelper {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerUser(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onResult(true, "Registration successful")
                } else {
                    onResult(false, it.exception?.message ?: "Registration failed")
                }
            }
    }

    fun loginUser(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onResult(true, "Login successful")
                } else {
                    onResult(false, it.exception?.message ?: "Login failed")
                }
            }
    }

    fun getCurrentUserEmail(): String? = auth.currentUser?.email
}
