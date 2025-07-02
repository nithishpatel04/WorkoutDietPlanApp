package com.example.workoutdietplanapp.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.example.workoutdietplanapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationFormScreen(navController: NavHostController) {
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
    )
    { innerPadding ->

        Image(
            painter = painterResource(id = R.drawable.gym_registration),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        RegistrationForm(
            modifier = Modifier.padding(innerPadding),
            navController = navController
        )
    }
}

@Composable
fun RegistrationForm(modifier: Modifier = Modifier, navController: NavHostController) {
    val goldColor = Color(0xFFE0AE57) // Mustard gold hex color

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var subscriptionType by remember { mutableStateOf("Monthly") }
    var gymPlan by remember { mutableStateOf(false) }
    var dietPlan by remember { mutableStateOf(false) }
    var motivation by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Incomplete Form", color = goldColor) },
            text = { Text("Please fill all fields before continuing.", color = Color.White) },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK", color = goldColor)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "Registration Form",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = goldColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter your name", color = goldColor) },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth(),

        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Enter your age", color = goldColor) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth(),

        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Enter your weight (kg)", color = goldColor) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth(),

        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Select Subscription Type", fontWeight = FontWeight.SemiBold, color = goldColor)
        Row {
            RadioButton(
                selected = subscriptionType == "Monthly",
                onClick = { subscriptionType = "Monthly" },
                colors = RadioButtonDefaults.colors(selectedColor = goldColor, unselectedColor = Color.White)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Monthly", color = Color.White)
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = subscriptionType == "Yearly",
                onClick = { subscriptionType = "Yearly" },
                colors = RadioButtonDefaults.colors(selectedColor = goldColor, unselectedColor = Color.White)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Yearly", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Select Plans", fontWeight = FontWeight.SemiBold, color = goldColor)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = gymPlan,
                onCheckedChange = { gymPlan = it },
                colors = CheckboxDefaults.colors(checkedColor = goldColor, uncheckedColor = Color.White)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Gym Plan", color = Color.White)
            Spacer(modifier = Modifier.width(16.dp))
            Checkbox(
                checked = dietPlan,
                onCheckedChange = { dietPlan = it },
                colors = CheckboxDefaults.colors(checkedColor = goldColor, uncheckedColor = Color.White)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Diet Plan", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = motivation,
            onValueChange = { motivation = it },
            label = { Text("What motivates you to do gym?", color = goldColor) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.White),

        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (name.isBlank() || age.isBlank() || weight.isBlank() || motivation.isBlank() || (!gymPlan && !dietPlan)) {
                    showDialog = true
                } else {
                    navController.navigate("home") // Adjust this route if needed
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Submit", color = goldColor)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegistrationFormPreview() {
    RegistrationFormScreen(navController = rememberNavController())
}
