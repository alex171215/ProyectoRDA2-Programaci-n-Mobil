package com.puce.ecotrip.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Esquema de color predeterminado para Modo Oscuro (Dark Theme).
 * Actúa como respaldo si el dispositivo es anterior a Android 12 (API 31)
 * o si el Color Dinámico está deshabilitado.
 */
private val DarkColorScheme = darkColorScheme(
    primary = EcoPrimaryDark,
    onPrimary = EcoOnPrimaryDark,
    primaryContainer = EcoPrimaryContainerDark,
    onPrimaryContainer = EcoOnPrimaryContainerDark,
    secondary = EcoSecondaryDark,
    onSecondary = EcoOnSecondaryDark,
    secondaryContainer = EcoSecondaryContainerDark,
    onSecondaryContainer = EcoOnSecondaryContainerDark,
    tertiary = EcoTertiaryDark,
    onTertiary = EcoOnTertiaryDark,
    tertiaryContainer = EcoTertiaryContainerDark,
    onTertiaryContainer = EcoOnTertiaryContainerDark,
    error = EcoErrorDark,
    onError = EcoOnErrorDark,
    errorContainer = EcoErrorContainerDark,
    onErrorContainer = EcoOnErrorContainerDark,
    background = EcoBackgroundDark,
    onBackground = EcoOnBackgroundDark,
    surface = EcoSurfaceDark,
    onSurface = EcoOnSurfaceDark,
    surfaceVariant = EcoSurfaceVariantDark,
    onSurfaceVariant = EcoOnSurfaceVariantDark,
    surfaceContainerLowest = EcoSurfaceContainerLowestDark,
    surfaceContainerLow = EcoSurfaceContainerLowDark,
    surfaceContainer = EcoSurfaceContainerDark,
    surfaceContainerHigh = EcoSurfaceContainerHighDark,
    surfaceContainerHighest = EcoSurfaceContainerHighestDark,
    outline = EcoOutlineDark,
    outlineVariant = EcoOutlineVariantDark
)

/**
 * Esquema de color predeterminado para Modo Claro (Light Theme).
 * Actúa como respaldo si el dispositivo es anterior a Android 12 (API 31)
 * o si el Color Dinámico está deshabilitado.
 */
private val LightColorScheme = lightColorScheme(
    primary = EcoPrimaryLight,
    onPrimary = EcoOnPrimaryLight,
    primaryContainer = EcoPrimaryContainerLight,
    onPrimaryContainer = EcoOnPrimaryContainerLight,
    secondary = EcoSecondaryLight,
    onSecondary = EcoOnSecondaryLight,
    secondaryContainer = EcoSecondaryContainerLight,
    onSecondaryContainer = EcoOnSecondaryContainerLight,
    tertiary = EcoTertiaryLight,
    onTertiary = EcoOnTertiaryLight,
    tertiaryContainer = EcoTertiaryContainerLight,
    onTertiaryContainer = EcoOnTertiaryContainerLight,
    error = EcoErrorLight,
    onError = EcoOnErrorLight,
    errorContainer = EcoErrorContainerLight,
    onErrorContainer = EcoOnErrorContainerLight,
    background = EcoBackgroundLight,
    onBackground = EcoOnBackgroundLight,
    surface = EcoSurfaceLight,
    onSurface = EcoOnSurfaceLight,
    surfaceVariant = EcoSurfaceVariantLight,
    onSurfaceVariant = EcoOnSurfaceVariantLight,
    surfaceContainerLowest = EcoSurfaceContainerLowestLight,
    surfaceContainerLow = EcoSurfaceContainerLowLight,
    surfaceContainer = EcoSurfaceContainerLight,
    surfaceContainerHigh = EcoSurfaceContainerHighLight,
    surfaceContainerHighest = EcoSurfaceContainerHighestLight,
    outline = EcoOutlineLight,
    outlineVariant = EcoOutlineVariantLight
)

/**
 * Tema principal de EcoTrip 2026.
 *
 * Implementa Color Dinámico (Material You):
 * A partir de Android 12 (API 31+), el motor Monet extrae los tonos predominantes
 * del fondo de pantalla del usuario mediante [dynamicLightColorScheme] o [dynamicDarkColorScheme].
 * Toda la UI que consuma tokens de [MaterialTheme.colorScheme] se transformará armoniosamente.
 *
 * @param darkTheme Define si se activa la variante oscura (por defecto toma la configuración del sistema).
 * @param dynamicColor Habilita la extracción dinámica de colores del wallpaper (disponible en API 31+).
 * @param content Contenido composable que heredará este sistema de diseño semántico.
 */
@Composable
fun EcoTripTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Material You dinámico en Android 12+ (API 31 / 36)
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        // Esquemas temáticos ecológicos de fallback
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}