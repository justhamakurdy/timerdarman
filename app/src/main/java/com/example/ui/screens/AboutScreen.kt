package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.MeshBackground
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    
    Box(modifier = Modifier.fillMaxSize()) {
        MeshBackground()
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("دەربارە", style = MaterialTheme.typography.titleLarge, color = MedicalCyan) },
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "دەرمانژمێر",
                            style = MaterialTheme.typography.displaySmall,
                            color = MedicalCyan
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            "ئەم ئەپڵیکەیشنە دروستکراوە بۆ هاوکاریکردنی نەخۆشەکان لە بەڕێوەبردنی تەندروستییان بە شێوەیەکی کاریگەر لە ڕێگەی بیرخستنەوەی وەرگرتنی دەرمان لە کاتی خۆیدا.",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary
                        )
                    }
                }
                
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                        Text("بۆ پشتگیری کردنی پەرەپێدەر", style = MaterialTheme.typography.titleSmall, color = MedicalPurple)
                        Text(
                            "پشتگیرییەکانتان هاندەرمانە بۆ بەردەوامبوون و دروستکردنی بەرنامەی زیاتر.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = TextAlign.End,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        ContactItem(icon = Icons.Default.Phone, label = "Asiacell", value = "07756294369")
                        ContactItem(icon = Icons.Default.AccountBalanceWallet, label = "Fib", value = "07756294369")
                    }
                }
                
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                        Text("تۆڕە کۆمەڵایەتییەکان", style = MaterialTheme.typography.titleSmall, color = MedicalPurple)
                        Text(
                            "ئەگەر پێشنیار، ڕەخنە، یان هەر سەرنجێکتان هەبێت، دڵخۆش دەبین بە بیستنی لە ڕێگەی ئەم تۆڕانەوە.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = TextAlign.End,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilledTonalButton(
                                onClick = { 
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/share/14eYB5ZPi7q/"))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = MedicalCyan.copy(alpha = 0.15f),
                                    contentColor = MedicalCyan
                                ),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Icon(Icons.Default.Facebook, contentDescription = "Facebook")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Facebook", style = MaterialTheme.typography.labelMedium)
                            }
                            
                            FilledTonalButton(
                                onClick = { 
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/k_2FH"))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = MedicalPurple.copy(alpha = 0.15f),
                                    contentColor = MedicalPurple
                                ),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Telegram")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Telegram", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    "Version 1.0.0",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun ContactItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    val context = LocalContext.current
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                val clip = android.content.ClipData.newPlainText("Contact Number", value)
                clipboard.setPrimaryClip(clip)
                android.widget.Toast.makeText(context, "ژمارەکە کۆپی کرا", android.widget.Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                Icons.Default.ContentCopy,
                contentDescription = "Copy",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                modifier = Modifier.size(18.dp)
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Text(value, style = MaterialTheme.typography.bodyMedium, color = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text("$label :", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.tertiary)
        Spacer(modifier = Modifier.width(12.dp))
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
    }
}
