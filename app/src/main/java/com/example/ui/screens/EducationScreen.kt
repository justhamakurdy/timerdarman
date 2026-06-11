package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.MeshBackground
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationScreen(
    onNavigateBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        MeshBackground()
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("زانیاری", style = MaterialTheme.typography.titleLarge, color = MedicalCyan) },
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
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.End
            ) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            Icons.Default.TipsAndUpdates,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MedicalCyan
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            "سودی وەرگرتنی دەرمان لە کاتی خۆیدا",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            color = MedicalCyan
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                EducationItem(
                    title = "مانەوەی ڕێژەی دەرمان لە خوێندا",
                    content = "وەرگرتنی دەرمان لە کاتی دیاریکراودا یارمەتیدەرە بۆ ئەوەی ڕێژەی دەرمانەکە لە ناو خوێندا بە جێگیری بمێنێتەوە، ئەمەش وادەکات دەرمانەکە باشترین کاریگەری هەبێت."
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                EducationItem(
                    title = "خێراکردنی چاکبوونەوە",
                    content = "کاتێک دەرمان بە ڕێکوپێکی وەردەگریت، جەستەت باشتر بەرەنگاری نەخۆشییەکە دەبێتەوە و پرۆسەی چاکبوونەوە خێراتر دەکات."
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                EducationItem(
                    title = "ڕێگری لە بەرگری دەرمان",
                    content = "بە تایبەت لە وەرگرتنی دژە بەکتریاکان (Antibiotics)، پشتگوێخستنی کاتەکان وادەکات بەکتریاکان بەهێزتر بن و دەرمانەکە کاریگەری نەمێنێت لە داهاتوودا."
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    "تەندروستیت سەرمایەی ژیانتە، تکایە ڕێنماییەکانی پزیشک پشتگوێ مەخە.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = MedicalPurple
                )
            }
        }
    }
}

@Composable
fun EducationItem(title: String, content: String) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = MedicalPurple.copy(alpha = 0.3f)
    ) {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MedicalCyan,
                textAlign = TextAlign.End
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.End,
                color = TextPrimary.copy(alpha = 0.9f)
            )
        }
    }
}
