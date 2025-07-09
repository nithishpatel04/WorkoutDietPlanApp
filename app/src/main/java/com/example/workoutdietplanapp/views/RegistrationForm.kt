package com.example.workoutdietplanapp.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.workoutdietplanapp.viewmodel.UserViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.workoutdietplanapp.R
import com.example.workoutdietplanapp.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationForm(navController: NavHostController, userViewModel: UserViewModel, modifier: Modifier) {
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
            // Background image
            Image(
                painter = painterResource(id = R.drawable.gym_registration),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Foreground form
            RegistrationFormScreen(
                navController = navController,
                userViewModel = userViewModel,
                modifier = Modifier.padding(innerPadding)

            )
        }
    }
}



@Composable
fun RegistrationFormScreen(navController: NavHostController, userViewModel: UserViewModel, modifier: Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var subscriptionType by remember { mutableStateOf("Monthly") }
    var gymPlan by remember { mutableStateOf(false) }
    var dietPlan by remember { mutableStateOf(false) }
    var motivation by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email",  color = Color.White) },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password",  color = Color.White,) },
            visualTransformation = PasswordVisualTransformation(),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name",  color = Color.White,) },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age",  color = Color.White,) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)",  color = Color.White) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text("Subscription Type",  color = Color.White)
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
        Text("Plans",  color = Color.White,)
        Row {
            Checkbox(
                checked = gymPlan,
                onCheckedChange = { gymPlan = it }
            )
            Text("Gym Plan",  color = Color.White,)
            Spacer(modifier = Modifier.width(16.dp))
            Checkbox(
                checked = dietPlan,
                onCheckedChange = { dietPlan = it }
            )
            Text("Diet Plan",  color = Color.White)
            Spacer(modifier = Modifier.width(16.dp))
        }

        OutlinedTextField(
            value = motivation,
            onValueChange = { motivation = it },
            label = { Text("Motivation",  color = Color.White,) },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (name.isNotBlank() && age.isNotBlank() && weight.isNotBlank() && motivation.isNotBlank()) {
                    userViewModel.updateProfile(
                        email = email,
                        password = password,
                        name = name,
                        age = age.toInt(),
                        weight = weight.toFloat(),
                        subscriptionType = subscriptionType,
                        gymPlan = gymPlan,
                        dietPlan = dietPlan,
                        motivation = motivation
                    )

                    // Optionally navigate or just stay
                    navController.navigate(Route.Home.routeName)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}

