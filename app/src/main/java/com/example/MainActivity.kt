package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.ui.DarmanNavGraph
import com.example.ui.MedicationViewModel
import com.example.ui.theme.DarmanZhmerTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      DarmanZhmerTheme {
        val navController = rememberNavController()
        val viewModel: MedicationViewModel = viewModel()
        
        DarmanNavGraph(
          navController = navController,
          viewModel = viewModel
        )
      }
    }
  }
}
