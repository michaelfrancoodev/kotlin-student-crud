package com.michaelfrancoodev.studentcrud.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Light color scheme
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = SurfaceLight,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = TextPrimary,

    secondary = Secondary,
    onSecondary = SurfaceLight,
    secondaryContainer = SecondaryLight,
    onSecondaryContainer = TextPrimary,

    tertiary = CoursePurple,
    onTertiary = SurfaceLight,
    tertiaryContainer = CoursePurple,
    onTertiaryContainer = TextPrimary,

    error = Error,
    onError = SurfaceLight,
    errorContainer = Error,
    onErrorContainer = TextPrimary,

    background = BackgroundLight,
    onBackground = TextPrimary,

    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundLight,
    onSurfaceVariant = TextSecondary,

    outline = Divider,
    outlineVariant = Divider,

    inverseSurface = TextPrimary,
    inverseOnSurface = SurfaceLight,
    inversePrimary = PrimaryLight
)

// Dark color scheme
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLight,
    onPrimary = TextPrimary,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = TextPrimaryDark,

    secondary = SecondaryLight,
    onSecondary = TextPrimary,
    secondaryContainer = SecondaryDark,
    onSecondaryContainer = TextPrimaryDark,

    tertiary = CoursePurple,
    onTertiary = TextPrimaryDark,
    tertiaryContainer = CoursePurple,
    onTertiaryContainer = TextPrimaryDark,

    error = Error,
    onError = TextPrimaryDark,
    errorContainer = Error,
    onErrorContainer = TextPrimaryDark,

    background = BackgroundDark,
    onBackground = TextPrimaryDark,

    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = BackgroundDark,
    onSurfaceVariant = TextSecondaryDark,

    outline = DividerDark,
    outlineVariant = DividerDark,

    inverseSurface = SurfaceLight,
    inverseOnSurface = TextPrimary,
    inversePrimary = Primary
)

/**
 * Soma Smart Theme
 *
 * @param darkTheme Whether to use dark theme (follows system by default)
 * @param dynamicColor Whether to use dynamic color (Android 12+)
 * @param content The composable content to apply theme to
 */
@Composable
fun SomaSmartTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
