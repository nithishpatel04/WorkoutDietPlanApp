package com.example.workoutdietplanapp.views

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.workoutdietplanapp.R
import com.example.workoutdietplanapp.firebase.FirebaseDatabaseHelper
import com.example.workoutdietplanapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavHostController, userViewModel: UserViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "💪 Stronger'n Better 💪",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.gym_buddies),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            SignInForm(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                userViewModel = userViewModel
            )
        }
    }
}

@Composable
fun SignInForm(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address", color = Color.White) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = Color.White) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val emailTrimmed = email.trim()
                val passwordTrimmed = password.trim()
                if (emailTrimmed.isNotBlank() && passwordTrimmed.isNotBlank()) {
                    Log.d("SignIn", "Attempting login with email: $emailTrimmed")
                    auth.signInWithEmailAndPassword(emailTrimmed, passwordTrimmed)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userEmail = auth.currentUser?.email ?: ""
                                userViewModel.login(userEmail)

                                FirebaseDatabaseHelper.fetchUserData(auth.currentUser?.uid ?: "") { user ->
                                    if (user != null) {
                                        userViewModel.setUser(
                                            user.name, user.email, user.age,
                                            height = user.height,
                                            weight = user.weight
                                        )
                                        Toast.makeText(context, "Welcome ${user.name}", Toast.LENGTH_SHORT).show()
                                        navController.navigate("home")
                                    } else {
                                        Toast.makeText(context, "Failed to load user profile.", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } else {
                                Log.e("SignIn", "Login failed", task.exception)
                                Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("SignIn", "FailureListener", exception)
                            Toast.makeText(context, "Login failed: ${exception.message}", Toast.LENGTH_LONG).show()
                        }
                } else {
                    Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign In")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Don't have an account?",
                color = Color.Blue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    navController.navigate("registration")
                }
            )
        }
    }
}
