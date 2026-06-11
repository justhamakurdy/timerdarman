package com.example.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ui.screens.*

@Composable
fun DarmanNavGraph(
    navController: NavHostController,
    viewModel: MedicationViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                viewModel = viewModel, 
                onNavigateToAdd = { navController.navigate("add") }, 
                onNavigateToEdit = { id -> navController.navigate("edit/$id") },
                onNavigateToEdu = { navController.navigate("edu") }, 
                onNavigateToAbout = { navController.navigate("about") },
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }
        composable("add") {
            AddMedicationScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
        }
        composable("edit/{medicationId}") { backStackEntry ->
            val medicationId = backStackEntry.arguments?.getString("medicationId")?.toIntOrNull() ?: -1
            EditMedicationScreen(
                medicationId = medicationId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("edu") {
            EducationScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable("about") {
            AboutScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable("settings") {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
