package com.example.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Medication
import com.example.ui.MedicationViewModel
import com.example.ui.components.GlassCard
import com.example.ui.components.MeshBackground
import com.example.ui.theme.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMedicationScreen(
    medicationId: Int,
    viewModel: MedicationViewModel,
    onNavigateBack: () -> Unit
) {
    var medication by remember { mutableStateOf<Medication?>(null) }
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var hour by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) }
    var minute by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.MINUTE)) }
    var isActive by remember { mutableStateOf(true) }

    LaunchedEffect(medicationId) {
        val med = viewModel.getMedicationById(medicationId)
        if (med != null) {
            medication = med
            name = med.name
            dosage = med.dosage
            hour = med.alarmHour
            minute = med.alarmMinute
            isActive = med.isActive
        }
    }
    
    val context = LocalContext.current
    val timePickerDialog = TimePickerDialog(
        context,
        { _, h, m ->
            hour = h
            minute = m
        },
        hour,
        minute,
        true
    )

    Box(modifier = Modifier.fillMaxSize()) {
        MeshBackground()
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("دەستکاریکردنی دەرمان", style = MaterialTheme.typography.titleLarge, color = MedicalCyan) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MedicalCyan)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Box(modifier = Modifier.fillMaxWidth()) { Text("ناوی دەرمان", modifier = Modifier.align(Alignment.CenterEnd)) } },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End, color = TextPrimary),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MedicalCyan,
                                unfocusedBorderColor = GlassStroke,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedLabelColor = MedicalCyan,
                                unfocusedLabelColor = TextSecondary
                            ),
                            shape = MaterialTheme.shapes.medium
                        )
                        
                        OutlinedTextField(
                            value = dosage,
                            onValueChange = { dosage = it },
                            label = { Box(modifier = Modifier.fillMaxWidth()) { Text("بڕ", modifier = Modifier.align(Alignment.CenterEnd)) } },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End, color = TextPrimary),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MedicalCyan,
                                unfocusedBorderColor = GlassStroke,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedLabelColor = MedicalCyan,
                                unfocusedLabelColor = TextSecondary
                            ),
                            shape = MaterialTheme.shapes.medium
                        )
                        
                        Button(
                            onClick = { timePickerDialog.show() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MedicalPurple.copy(alpha = 0.15f),
                                contentColor = MedicalPurple
                            ),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text("گۆڕینی کات: ${String.format("%02d:%02d", hour, minute)}", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1.0f))
                
                Surface(
                    onClick = {
                        if (name.isNotBlank() && dosage.isNotBlank()) {
                            viewModel.updateMedication(medicationId, name, dosage, hour, minute, isActive)
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    shape = MaterialTheme.shapes.large,
                    color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier.background(
                            brush = Brush.linearGradient(
                                colors = listOf(MedicalCyan, MedicalPurple)
                            )
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "پاشکەوتکردنی گۆڕانکارییەکان", 
                            style = MaterialTheme.typography.titleMedium, 
                            color = MedicalDeepBlue,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}
