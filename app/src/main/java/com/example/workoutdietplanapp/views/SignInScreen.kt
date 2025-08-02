package com.example.workoutdietplanapp.views

import android.content.Context
import android.util.Log
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavHostController, userViewModel: UserViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope() // CoroutineScope here

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
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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
                userViewModel = userViewModel,
                snackbarHostState = snackbarHostState,
                scope = scope // Pass CoroutineScope here
            )
        }
    }
}

@Composable
fun SignInForm(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    userViewModel: UserViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope // Use CoroutineScope here
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    var email by remember { mutableStateOf(sharedPref.getString("email", "") ?: "") }
    var password by remember { mutableStateOf(sharedPref.getString("password", "") ?: "") }

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
                                // Save credentials to SharedPreferences
                                with(sharedPref.edit()) {
                                    putString("email", emailTrimmed)
                                    putString("password", passwordTrimmed)
                                    apply()
                                }

                                val userEmail = auth.currentUser?.email ?: ""
                                userViewModel.login(userEmail)

                                FirebaseDatabaseHelper.fetchUserData(auth.currentUser?.uid ?: "") { user ->
                                    if (user != null) {
                                        userViewModel.setUser(
                                            name = user.name,
                                            email = user.email,
                                            age = user.age,
                                            height = user.height,
                                            weight = user.weight,
                                            goal = user.goal
                                        )
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Welcome ${user.name}")
                                        }
                                        navController.navigate("home")
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Failed to load user profile.")
                                        }
                                    }
                                }
                            } else {
                                Log.e("SignIn", "Login failed", task.exception)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Login failed: ${task.exception?.message}")
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("SignIn", "FailureListener", exception)
                            scope.launch {
                                snackbarHostState.showSnackbar("Login failed: ${exception.message}")
                            }
                        }
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar("Please enter email and password")
                    }
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign In", color = Color.White)
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
