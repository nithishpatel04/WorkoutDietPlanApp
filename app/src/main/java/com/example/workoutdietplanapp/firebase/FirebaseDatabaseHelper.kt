package com.example.workoutdietplanapp.firebase

import com.example.workoutdietplanapp.viewmodel.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.tasks.await

object FirebaseDatabaseHelper {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("users")

    // Register user in Firebase Authentication (Email/Password)
    fun registerUserAuth(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Authentication successful")
                } else {
                    callback(false, task.exception?.message ?: "Authentication failed")
                }
            }
    }

    // Save user profile data in Realtime Database (callback version)
    fun saveUserData(user: User, callback: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            callback(false)
            return
        }

        val uid = currentUser.uid
        val userMap = mapOf(
            "email" to user.email,
            "password" to user.password,
            "name" to user.name,
            "age" to user.age,
            "height" to user.height,
            "weight" to user.weight,
            "goal" to user.goal,
            "subscriptionType" to user.subscriptionType,
            "gymPlan" to user.gymPlan,
            "dietPlan" to user.dietPlan,
            "isLoggedIn" to user.isLoggedIn
        )

        database.child(uid).setValue(userMap)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    // Fetch user profile data from Realtime Database by UID
    fun fetchUserData(email: String, callback: (User?) -> Unit) {
        val currentUser = auth.currentUser ?: return callback(null)
        val uid = currentUser.uid

        database.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = User(
                        email = snapshot.child("email").getValue(String::class.java) ?: "",
                        password = snapshot.child("password").getValue(String::class.java) ?: "",
                        name = snapshot.child("name").getValue(String::class.java) ?: "",
                        age = snapshot.child("age").getValue(Int::class.java) ?: 0,
                        height = snapshot.child("height").getValue(Float::class.java) ?: 0f,
                        weight = snapshot.child("weight").getValue(Float::class.java) ?: 0f,
                        goal = snapshot.child("goal").getValue(String::class.java) ?: "",
                        subscriptionType = snapshot.child("subscriptionType")
                            .getValue(String::class.java) ?: "",
                        gymPlan = snapshot.child("gymPlan").getValue(Boolean::class.java) ?: false,
                        dietPlan = snapshot.child("dietPlan").getValue(Boolean::class.java)
                            ?: false,
                        isLoggedIn = true
                    )
                    callback(user)
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }

    // -----------------------------
    // SUSPEND VERSION for Coroutines
    // -----------------------------

    // Suspend function to save user data
    suspend fun saveUserDataSuspend(user: User): Boolean {
        val currentUser = auth.currentUser ?: return false
        val uid = currentUser.uid

        val userMap = mapOf(
            "email" to user.email,
            "password" to user.password,
            "name" to user.name,
            "age" to user.age,
            "height" to user.height,
            "weight" to user.weight,
            "goal" to user.goal,
            "subscriptionType" to user.subscriptionType,
            "gymPlan" to user.gymPlan,
            "dietPlan" to user.dietPlan,
            "isLoggedIn" to user.isLoggedIn
        )

        return try {
            database.child(uid).setValue(userMap).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}