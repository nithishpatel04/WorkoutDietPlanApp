package com.example.workoutdietplanapp.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.workoutdietplanapp.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavHostController, userViewModel: UserViewModel) {

    val user by userViewModel.user.collectAsState()

    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf(user.password) }
    var age by remember { mutableStateOf(user.age.toString()) }
    var height by remember { mutableStateOf(user.height.toString()) }
    var weight by remember { mutableStateOf(user.weight.toString()) }
    var motivation by remember { mutableStateOf(user.goal) }

    var showOldPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "💪 Stronger'n Better 💪",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            // Old Password
            OutlinedTextField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                label = { Text("Old Password") },
                singleLine = true,
                visualTransformation = if (showOldPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val iconText = if (showOldPassword) "Hide" else "Show"
                    TextButton(onClick = { showOldPassword = !showOldPassword }) {
                        Text(iconText)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // New Password
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password") },
                singleLine = true,
                visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val iconText = if (showNewPassword) "Hide" else "Show"
                    TextButton(onClick = { showNewPassword = !showNewPassword }) {
                        Text(iconText)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = age,
                onValueChange = { if (it.all { ch -> ch.isDigit() }) age = it },
                label = { Text("Age") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = height,
                onValueChange = { if (it.all { ch -> ch.isDigit() || ch == '.' }) height = it },
                label = { Text("Height (cm)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = weight,
                onValueChange = { if (it.all { ch -> ch.isDigit() || ch == '.' }) weight = it },
                label = { Text("Weight (kg)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = motivation,
                onValueChange = { motivation = it },
                label = { Text("Motivation") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            if (message.isNotEmpty()) {
                Text(text = message, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    isLoading = true
                    val ageInt = age.toIntOrNull() ?: 0
                    val heightFloat = height.toFloatOrNull() ?: 0f
                    val weightFloat = weight.toFloatOrNull() ?: 0f

                    userViewModel.updateProfile(
                        email = email,
                        oldPassword = oldPassword,
                        newPassword = newPassword,
                        name = name,
                        age = ageInt,
                        height = heightFloat,
                        weight = weightFloat,
                        motivation = motivation
                    ) { success, resultMessage ->
                        isLoading = false
                        message = if (success) "Profile updated successfully" else "Update failed: $resultMessage"
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Save")
                }
            }
        }
    }
}
