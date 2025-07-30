package com.example.workoutdietplanapp.views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.workoutdietplanapp.R
import com.example.workoutdietplanapp.navigation.Route
import com.example.workoutdietplanapp.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietScreen(navController: NavHostController, userViewModel: UserViewModel) {
    var selectedIndex by remember { mutableStateOf(1) } // Diet Plan tab selected
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Dialog controls
    var showProteinDialog by remember { mutableStateOf(false) }
    var showBMIDialog by remember { mutableStateOf(false) }
    var showCalorieDialog by remember { mutableStateOf(false) }

    val user by userViewModel.user.collectAsState()
    val level by userViewModel.level.collectAsState()
    val dietPlan by userViewModel.dietPlan.collectAsState()

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
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF2196f3), Color(0xFF3f51b5))
                    )
                ),
                containerColor = Color.Black,
                contentColor = Color.White
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },
                    label = { Text("Home", color = Color.White) },
                    selected = selectedIndex == 0,
                    onClick = {
                        selectedIndex = 0
                        navController.navigate(Route.Home.routeName)
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Diet Plan", tint = Color.White) },
                    label = { Text("Diet Plan", color = Color.White) },
                    selected = selectedIndex == 1,
                    onClick = {
                        selectedIndex = 1
                        navController.navigate(Route.DietPlan.routeName)
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White) },
                    label = { Text("Profile", color = Color.White) },
                    selected = selectedIndex == 2,
                    onClick = { selectedIndex = 2
                        navController.navigate(Route.Profile.routeName)}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White) },
                    label = { Text("Logout", color = Color.White) },
                    selected = false,
                    onClick = { showLogoutDialog = true }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.home_screen),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Hello, ${user.name}!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )


                // Cards Section with icons
                HealthToolCard(
                    title = "Protein Calculator",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = "Protein Icon",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                ) {
                    showProteinDialog = true
                }
                Spacer(modifier = Modifier.height(12.dp))
                HealthToolCard(
                    title = "BMI Calculator",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = "BMI Icon",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                ) {
                    showBMIDialog = true
                }
                Spacer(modifier = Modifier.height(12.dp))
                HealthToolCard(
                    title = "Food Calorie Finder",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Fastfood,
                            contentDescription = "Food Icon",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                ) {
                    showCalorieDialog = true
                }

            }
        }
    }

    if (showProteinDialog) {
        ProteinCalculatorDialog { showProteinDialog = false }
    }
    if (showBMIDialog) {
        BMICalculatorDialog(userViewModel) { showBMIDialog = false }
    }
    if (showCalorieDialog) {
        CalorieFinderDialog { showCalorieDialog = false }
    }
}

@Composable
fun HealthToolCard(
    title: String,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
            }
            Text(text = title, style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
    }
}

// -------- Protein Calculator Dialog ---------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProteinCalculatorDialog(onDismiss: () -> Unit) {
    var weight by remember { mutableStateOf("") }
    val intensities = listOf("Light", "Moderate", "Intense")
    var intensity by remember { mutableStateOf("Moderate") }
    var proteinNeed by remember { mutableStateOf<Float?>(null) }
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    fun calculateProtein(weightKg: Float, intensity: String): Float {
        val multiplier = when (intensity) {
            "Light" -> 0.9f
            "Moderate" -> 1.3f
            "Intense" -> 1.8f
            else -> 1.3f
        }
        return weightKg * multiplier
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Protein Calculator") },
        text = {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Intensity Dropdown
                Box {
                    OutlinedTextField(
                        value = intensity,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Intensity") },
                        trailingIcon = {
                            Icon(
                                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Toggle Intensity Dropdown",
                                modifier = Modifier.clickable { expanded = !expanded }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        intensities.forEach { level ->
                            DropdownMenuItem(
                                text = { Text(level) },
                                onClick = {
                                    intensity = level
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val weightVal = weight.toFloatOrNull()
                        if (weightVal == null || weightVal <= 0f) {
                            Toast.makeText(context, "Please enter a valid weight", Toast.LENGTH_SHORT).show()
                            proteinNeed = null
                        } else {
                            proteinNeed = calculateProtein(weightVal, intensity)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Calculate Protein Need")
                }

                proteinNeed?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Recommended Protein Intake: %.1f grams per day".format(it),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}

// -------- BMI Calculator Dialog ---------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMICalculatorDialog(userViewModel: UserViewModel, onDismiss: () -> Unit) {
    val weight by userViewModel.weightKg.collectAsState(initial = 70f)
    val heightCm by userViewModel.heightCm.collectAsState(initial = 170f)

    val heightM = heightCm / 100f
    val bmi = if (heightM > 0) weight / (heightM * heightM) else 0f
    val bmiFormatted = String.format("%.1f", bmi)
    val bmiCategory = when {
        bmi < 18.5 -> "Underweight"
        bmi < 25 -> "Normal weight"
        bmi < 30 -> "Overweight"
        else -> "Obese"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("BMI Calculator") },
        text = {
            Column {
                Text("Weight: $weight kg", color = Color.Black)
                Text("Height: $heightCm cm", color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Your BMI is:")
                Text(bmiFormatted, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))
                Text("Category: $bmiCategory")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}

// -------- Food Calorie Finder Dialog with dropdown ---------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalorieFinderDialog(onDismiss: () -> Unit) {
    val foodCalorieMap = mapOf(
        "Apple" to 95,
        "Banana" to 105,
        "Egg" to 78,
        "Chicken Breast" to 165,
        "Rice (1 cup)" to 206,
        "Orange" to 62,
        "Broccoli" to 55,
        "Almonds" to 164,
        "Salmon" to 208,
        "Yogurt" to 59
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedFood by remember { mutableStateOf(foodCalorieMap.keys.first()) }
    var calories by remember { mutableStateOf<Int?>(null) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Food Calorie Finder") },
        text = {
            Column {
                Box {
                    OutlinedTextField(
                        value = selectedFood,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select food") },
                        trailingIcon = {
                            Icon(
                                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                Modifier.clickable { expanded = !expanded }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        foodCalorieMap.keys.forEach { food ->
                            DropdownMenuItem(
                                text = { Text(food) },
                                onClick = {
                                    selectedFood = food
                                    expanded = false
                                    calories = null // reset calories on new selection
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val cal = foodCalorieMap[selectedFood]
                        if (cal != null) {
                            calories = cal
                        } else {
                            Toast.makeText(context, "Food not found", Toast.LENGTH_SHORT).show()
                            calories = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Find Calories")
                }

                calories?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Calories in $selectedFood: $it kcal", style = MaterialTheme.typography.titleMedium)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}
