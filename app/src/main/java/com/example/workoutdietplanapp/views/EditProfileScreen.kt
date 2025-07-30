package com.example.workoutdietplanapp.views

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.workoutdietplanapp.viewmodel.UserViewModel
import com.example.workoutdietplanapp.viewmodel.User
import com.example.workoutdietplanapp.firebase.FirebaseDatabaseHelper
import com.google.firebase.auth.FirebaseAuth

@Composable
fun EditProfileScreen(navController: NavHostController, userViewModel: UserViewModel) {
    val context = LocalContext.current
    val user by userViewModel.user.collectAsState()

    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var password by remember { mutableStateOf(user.password) }
    var motivation by remember { mutableStateOf(user.goal) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Edit Profile", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )

        OutlinedTextField(
            value = motivation,
            onValueChange = { motivation = it },
            label = { Text("Motivation / Goal") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (name.isNotBlank() && password.isNotBlank() && motivation.isNotBlank()) {
                val updatedUser = User(
                    email = email,
                    password = password,
                    name = name,
                    age = user.age,
                    weight = user.weight,
                    subscriptionType = user.subscriptionType,
                    gymPlan = user.gymPlan,
                    dietPlan = user.dietPlan,
                    goal = motivation,
                    isLoggedIn = true
                )

                val currentUser = FirebaseAuth.getInstance().currentUser

                if (currentUser != null) {
                    currentUser.updatePassword(password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            FirebaseDatabaseHelper.saveUserData(updatedUser) { success ->
                                if (success) {
                                    userViewModel.updateUserLocally(name, password, motivation)
                                    Toast.makeText(context, "Profile updated!", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                } else {
                                    Toast.makeText(context, "Failed to update profile in database.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Failed to update password in Firebase Auth.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "No user is logged in.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Save Changes")
        }
    }
}
