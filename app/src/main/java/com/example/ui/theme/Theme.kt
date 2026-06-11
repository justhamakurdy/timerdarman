package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

private val DarmanShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(32.dp),
    extraLarge = RoundedCornerShape(40.dp)
)

private val DarmanDarkColorScheme = darkColorScheme(
    primary = MedicalCyan,
    onPrimary = MedicalDeepBlue,
    primaryContainer = MedicalCyan.copy(alpha = 0.2f),
    onPrimaryContainer = MedicalCyan,
    secondary = AccentSafe,
    onSecondary = MedicalDeepBlue,
    secondaryContainer = AccentSafe.copy(alpha = 0.2f),
    onSecondaryContainer = AccentSafe,
    tertiary = MedicalPurple,
    onTertiary = MedicalDeepBlue,
    tertiaryContainer = MedicalPurple.copy(alpha = 0.2f),
    onTertiaryContainer = MedicalPurple,
    background = MedicalDeepBlue,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceDark.copy(alpha = 0.7f),
    onSurfaceVariant = TextSecondary,
    outline = GlassStroke,
    error = AccentError,
    onError = Color.White
)

@Composable
fun DarmanZhmerTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DarmanDarkColorScheme,
        typography = Typography,
        shapes = DarmanShapes,
        content = content
    )
}
