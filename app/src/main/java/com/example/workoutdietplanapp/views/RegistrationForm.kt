package com.example.workoutdietplanapp.views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.workoutdietplanapp.R
import com.example.workoutdietplanapp.navigation.Route
import com.example.workoutdietplanapp.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationForm(
    navController: NavHostController,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var subscriptionType by remember { mutableStateOf("Monthly") }
    var gymPlan by remember { mutableStateOf(false) }
    var dietPlan by remember { mutableStateOf(false) }
    var motivation by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "💪 Stronger'n Better 💪",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
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
                painter = painterResource(id = R.drawable.gym_registration),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = Color.White) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = Color.White) },
                    visualTransformation = PasswordVisualTransformation(),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name", color = Color.White) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age", color = Color.White) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Height (cm)", color = Color.White) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight (kg)", color = Color.White) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text("Subscription Type", color = Color.White)
                Row {
                    RadioButton(
                        selected = subscriptionType == "Monthly",
                        onClick = { subscriptionType = "Monthly" }
                    )
                    Text("Monthly", color = Color.White)
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = subscriptionType == "Yearly",
                        onClick = { subscriptionType = "Yearly" }
                    )
                    Text("Yearly", color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Plans", color = Color.White)
                Row {
                    Checkbox(
                        checked = gymPlan,
                        onCheckedChange = { gymPlan = it }
                    )
                    Text("Gym Plan", color = Color.White)
                    Spacer(modifier = Modifier.width(16.dp))
                    Checkbox(
                        checked = dietPlan,
                        onCheckedChange = { dietPlan = it }
                    )
                    Text("Diet Plan", color = Color.White)
                }

                OutlinedTextField(
                    value = motivation,
                    onValueChange = { motivation = it },
                    label = { Text("Motivation", color = Color.White) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank() && name.isNotBlank() &&
                            age.isNotBlank() && height.isNotBlank() && weight.isNotBlank() && motivation.isNotBlank()
                        ) {
                            userViewModel.registerUser(
                                email = email,
                                password = password,
                                name = name,
                                age = age.toIntOrNull() ?: 0,
                                height = height.toFloatOrNull() ?: 0f,
                                weight = weight.toFloatOrNull() ?: 0f,
                                subscriptionType = subscriptionType,
                                gymPlan = gymPlan,
                                dietPlan = dietPlan,
                                motivation = motivation
                            ) { success, message ->
                                if (success) {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    navController.navigate(Route.SignIn.routeName) {
                                        popUpTo(Route.Registration.routeName) { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register")
                }
            }
        }
    }
}
