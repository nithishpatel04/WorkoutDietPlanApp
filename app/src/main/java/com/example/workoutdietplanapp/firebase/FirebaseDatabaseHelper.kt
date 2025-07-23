package com.example.workoutdietplanapp.firebase

import com.example.workoutdietplanapp.viewmodel.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

object FirebaseDatabaseHelper {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")

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

    // Save user profile data in Realtime Database under "users/{uid}"
    fun saveUserData(user: User, callback: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            callback(false)
            return
        }

        val uid = currentUser.uid
        // Write user data excluding password for security reasons if desired
        val userMap = mapOf(
            "email" to user.email,
            "name" to user.name,
            "age" to user.age,
            "weight" to user.weight,
            "goal" to user.goal,
            "subscriptionType" to user.subscriptionType,
            "gymPlan" to user.gymPlan,
            "dietPlan" to user.dietPlan
        )

        database.child(uid).setValue(userMap)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    // Fetch user profile data from Realtime Database by UID
    fun fetchUserData(uid: String, callback: (User?) -> Unit) {
        database.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val email = snapshot.child("email").getValue(String::class.java) ?: ""
                    val name = snapshot.child("name").getValue(String::class.java) ?: ""
                    val age = snapshot.child("age").getValue(Int::class.java) ?: 0
                    val weight = snapshot.child("weight").getValue(Double::class.java)?.toFloat() ?: 0f
                    val goal = snapshot.child("goal").getValue(String::class.java) ?: ""
                    val subscriptionType = snapshot.child("subscriptionType").getValue(String::class.java) ?: ""
                    val gymPlan = snapshot.child("gymPlan").getValue(Boolean::class.java) ?: false
                    val dietPlan = snapshot.child("dietPlan").getValue(Boolean::class.java) ?: false

                    val user = User(
                        email = email,
                        name = name,
                        age = age,
                        weight = weight,
                        goal = goal,
                        subscriptionType = subscriptionType,
                        gymPlan = gymPlan,
                        dietPlan = dietPlan,
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
}
