package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.example.data.Medication
import com.example.ui.MedicationViewModel
import com.example.ui.components.GlassCard
import com.example.ui.components.MeshBackground
import com.example.ui.theme.*
import android.Manifest
import android.os.Build

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewModel: MedicationViewModel,
    onNavigateToAdd: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onNavigateToEdu: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val medications by viewModel.allMedications.collectAsStateWithLifecycle()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        LaunchedEffect(Unit) {
            if (!permissionState.status.isGranted) {
                permissionState.launchPermissionRequest()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MeshBackground()
        
        Scaffold(
            floatingActionButton = {
                Surface(
                    onClick = onNavigateToAdd,
                    color = Color.Transparent,
                    shape = androidx.compose.foundation.shape.CircleShape,
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(MedicalCyan, MedicalPurple)
                                )
                            )
                            .border(2.dp, Color.White.copy(alpha = 0.2f), androidx.compose.foundation.shape.CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color.White.copy(alpha = 0.1f), androidx.compose.foundation.shape.CircleShape)
                        )
                        Icon(
                            Icons.Default.Add, 
                            contentDescription = "Add", 
                            modifier = Modifier.size(36.dp),
                            tint = Color.White
                        )
                    }
                }
            },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .statusBarsPadding() // Ensure it doesn't overlap status bar
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.End
            ) {
                // Header section with Profile/Settings and App Name
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onNavigateToSettings,
                            modifier = Modifier
                                .size(44.dp)
                                .background(GlassBackground, androidx.compose.foundation.shape.CircleShape)
                                .border(1.dp, GlassStroke, androidx.compose.foundation.shape.CircleShape)
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings", tint = MedicalCyan, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        IconButton(
                            onClick = onNavigateToEdu,
                            modifier = Modifier
                                .size(44.dp)
                                .background(GlassBackground, androidx.compose.foundation.shape.CircleShape)
                                .border(1.dp, GlassStroke, androidx.compose.foundation.shape.CircleShape)
                        ) {
                            Icon(Icons.Default.School, contentDescription = "Education", tint = MedicalPurple, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        IconButton(
                            onClick = onNavigateToAbout,
                            modifier = Modifier
                                .size(44.dp)
                                .background(GlassBackground, androidx.compose.foundation.shape.CircleShape)
                                .border(1.dp, GlassStroke, androidx.compose.foundation.shape.CircleShape)
                        ) {
                            Icon(Icons.Default.Info, contentDescription = "About", tint = MedicalCyan, modifier = Modifier.size(22.dp))
                        }
                    }
                    
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "بەخێربێیتەوە",
                            style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.sp),
                            color = TextSecondary
                        )
                        Text(
                            "دەرمانژمێر", 
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black), 
                            color = MedicalCyan
                        )
                    }
                }

                // Statistics/Status Section (Redesigned Hero Card)
                val activeCount = medications.count { it.isActive }
                val totalCount = medications.size
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GlassCard(
                        modifier = Modifier.weight(0.4f),
                        borderColor = MedicalPurple.copy(alpha = 0.3f)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "$activeCount",
                                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black),
                                color = MedicalPurple
                            )
                            Text(
                                text = "چالاک",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }
                    
                    val nextMed = medications.filter { it.isActive }.minByOrNull { it.alarmHour * 60 + it.alarmMinute }
                    GlassCard(
                        modifier = Modifier.weight(0.6f),
                        borderColor = MedicalCyan.copy(alpha = 0.3f)
                    ) {
                        Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = nextMed?.name ?: "بێ دەرمان",
                                style = MaterialTheme.typography.titleMedium,
                                color = MedicalCyan,
                                textAlign = TextAlign.End,
                                maxLines = 1
                            )
                            Text(
                                text = if (nextMed != null) "ژەمی داهاتوو ${String.format("%02d:%02d", nextMed.alarmHour, nextMed.alarmMinute)}" else "هیچ ژەمێک نییە",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    "خشتەی ئەمڕۆ",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.End,
                    color = TextPrimary,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
                
                if (medications.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize().padding(bottom = 80.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Info, 
                                contentDescription = null, 
                                modifier = Modifier.size(64.dp),
                                tint = GlassStroke
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "هیچ دەرمانێک زیاد نەکراوە\nبۆ دەستپێکردن پڵاسەکە داگرە",
                                textAlign = TextAlign.Center,
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        items(medications) { medication ->
                            MedicationItem(
                                medication = medication,
                                onToggle = { viewModel.toggleMedication(medication) },
                                onDelete = { viewModel.deleteMedication(medication) },
                                onEdit = { onNavigateToEdit(medication.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MedicationItem(
    medication: Medication,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("سڕینەوەی دەرمان", textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth()) },
            text = { Text("ئایا دڵنیای لە سڕینەوەی ${medication.name}؟", textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth()) },
            confirmButton = {
                TextButton(onClick = { 
                    onDelete()
                    showDialog = false 
                }) {
                    Text("بەڵێ")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("نەخێر")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.extraLarge
        )
    }

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onEdit,
        borderColor = if (medication.isActive) MedicalCyan.copy(alpha = 0.4f) else GlassStroke,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.background(Color.White.copy(alpha = 0.05f), androidx.compose.foundation.shape.CircleShape)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = AccentHotPink.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Switch(
                    checked = medication.isActive,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MedicalCyan,
                        checkedTrackColor = MedicalCyan.copy(alpha = 0.2f),
                        uncheckedThumbColor = TextSecondary.copy(alpha = 0.5f),
                        uncheckedTrackColor = Color.Transparent,
                        uncheckedBorderColor = GlassStroke
                    )
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = medication.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (medication.isActive) MedicalCyan else TextSecondary,
                )
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                    Text(
                        text = String.format("%02d:%02d", medication.alarmHour, medication.alarmMinute),
                        style = MaterialTheme.typography.labelLarge,
                        color = if (medication.isActive) MedicalPurple else TextSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(TextSecondary.copy(alpha = 0.3f), androidx.compose.foundation.shape.CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "بڕ: ${medication.dosage}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}
