package com.example.workoutdietplanapp.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.workoutdietplanapp.navigation.Route
import com.example.workoutdietplanapp.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, userViewModel: UserViewModel) {
    var selectedIndex by remember { mutableStateOf(0) }
    var showLogoutDialog by remember { mutableStateOf(false) }

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
                    onClick = { selectedIndex = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedIndex == 1,
                    onClick = {
                        selectedIndex = 1
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

        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            Text(
                text = if (isDietSelected) "Diet Plan for $level" else "Workout Plan for $level",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (isDietSelected) {
                dietPlan?.let { diet ->
                    Text("Protein: ${diet.proteinGrams}g")
                    Text("Water: ${diet.waterGlasses} glasses")
                    diet.extraTips.forEach { tip ->
                        Text("- $tip")
                    }
                }
            } else {
                LazyColumn {
                    items(workouts.size) { index ->
                        val w = workouts[index]
                        Text("${w.day} - ${w.workout}: ${w.variations} variations x ${w.reps} reps, ${w.maxWeightKg}kg")
                    }
                }
            }
        }
    }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    userViewModel.Logout() // lowercase 'logout'
                    navController.navigate(Route.SignIn.routeName) {
                        popUpTo(0) // Clear navigation stack
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

