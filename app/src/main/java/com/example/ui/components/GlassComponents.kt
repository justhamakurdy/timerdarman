package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(24.dp), // Increased rounded corners
    borderColor: Color = GlassStroke,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        onClick = onClick ?: {},
        enabled = onClick != null,
        modifier = modifier
            .clip(shape)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(borderColor, borderColor.copy(alpha = 0.1f)),
                    start = Offset(0f, 0f),
                    end = Offset(400f, 400f)
                ),
                shape = shape
            ),
        color = GlassBackground,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            content()
        }
    }
}

@Composable
fun MeshBackground() {
    Box(modifier = Modifier.fillMaxSize().background(
        brush = Brush.verticalGradient(
            colors = listOf(DashboardGradientStart, DashboardGradientMid, DashboardGradientEnd)
        )
    )) {
        Canvas(modifier = Modifier.fillMaxSize().blur(100.dp)) {
            // Main dynamic aura
            drawCircle(
                color = MedicalCyan.copy(alpha = 0.15f),
                radius = size.width * 1.5f,
                center = Offset(size.width * 0.9f, size.height * 0.2f)
            )
            // Accent glow
            drawCircle(
                color = MedicalPurple.copy(alpha = 0.15f),
                radius = size.width * 1.2f,
                center = Offset(size.width * 0.2f, size.height * 0.8f)
            )
            // Counter-balance glow
            drawCircle(
                color = MedicalCyan.copy(alpha = 0.08f),
                radius = size.width * 0.8f,
                center = Offset(size.width * 0.5f, size.height * 0.5f)
            )
        }
    }
}

@Composable
fun GlassBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(GlassBackground, MaterialTheme.shapes.small)
            .border(1.dp, GlassStroke, MaterialTheme.shapes.small)
            .padding(8.dp),
        content = content
    )
}
