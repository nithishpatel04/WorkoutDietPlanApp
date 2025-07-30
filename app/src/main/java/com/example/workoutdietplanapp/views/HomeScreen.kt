package com.example.workoutdietplanapp.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.workoutdietplanapp.R
import com.example.workoutdietplanapp.navigation.Route
import com.example.workoutdietplanapp.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, userViewModel: UserViewModel) {
    var selectedIndex by remember { mutableStateOf(0) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val user by userViewModel.user.collectAsState()
    val isDietSelected by userViewModel.isDietSelected.collectAsState()
    val level by userViewModel.level.collectAsState()
    val workouts by userViewModel.workouts.collectAsState()
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
            NavigationBar(containerColor = Color.Black, contentColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedIndex == 0,
                    onClick = {
                        selectedIndex = 0
                        // Stay on Home screen, no navigation needed
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Diet Plan") },
                    label = { Text("Diet Plan") },
                    selected = selectedIndex == 1,
                    onClick = {
                        selectedIndex = 1
                        navController.navigate(Route.DietPlan.routeName)
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedIndex == 2,
                    onClick = {
                        selectedIndex = 2
                        navController.navigate(Route.Profile.routeName)
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Logout, contentDescription = "Logout") },
                    label = { Text("Logout") },
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
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.home_screen),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Foreground Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Welcome, ${user.name}!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = if (isDietSelected) "Your Diet Plan for $level" else "Your Workout Plan for $level",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (isDietSelected) {
                    dietPlan?.let { diet ->
                        Text("• Protein: ${diet.proteinGrams}g", color = Color.White)
                        Text("• Water: ${diet.waterGlasses} glasses", color = Color.White)
                        Text("• Tips:", color = Color.White)
                        diet.extraTips.forEach { tip ->
                            Text("- $tip", color = Color.White)
                        }
                    } ?: Text("No diet plan available.", color = Color.White)
                } else {
                    if (workouts.isNotEmpty()) {
                        LazyColumn {
                            items(workouts.size) { index ->
                                val w = workouts[index]
                                Text("Day ${w.day} - ${w.workout}", color = Color.White)
                                Text("  → ${w.variations} variations x ${w.reps} reps @ ${w.maxWeightKg}kg", color = Color.White)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    } else {
                        Text("No workout plan available.", color = Color.White)
                    }
                }
            }
        }
    }

    // Logout Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    userViewModel.logout()
                    navController.navigate(Route.SignIn.routeName) {
                        popUpTo(0)
                    }
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
