package com.puce.ecotrip.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Paleta de colores para EcoTrip 2026 bajo lineamientos de Material Design 3 (Material You).
 *
 * NOTA DE ARQUITECTURA (Rol 3):
 * Todas las definiciones literales de color (Color(0x...)) residen EXCLUSIVAMENTE en este archivo.
 * Las pantallas y componentes de la UI consumen los roles semánticos expuestos a través de
 * MaterialTheme.colorScheme (ej. primary, surface, onSurfaceVariant), asegurando adaptabilidad
 * total ante temas claros/oscuros y extracción dinámica de papel tapiz (Monet Engine).
 */

// ==========================================
// PALETA MODO CLARO (Light Theme - Eco Friendly)
// ==========================================
val EcoPrimaryLight = Color(0xFF1B6C43)           // Verde bosque profundo (identidad ecológica)
val EcoOnPrimaryLight = Color(0xFFFFFFFF)         // Texto sobre primario
val EcoPrimaryContainerLight = Color(0xFFA6F5C0)  // Contenedor verde menta suave
val EcoOnPrimaryContainerLight = Color(0xFF002110)// Texto sobre contenedor primario

val EcoSecondaryLight = Color(0xFF4F6352)         // Salvia silvestre neutro
val EcoOnSecondaryLight = Color(0xFFFFFFFF)
val EcoSecondaryContainerLight = Color(0xFFD2E8D3)// Contenedor salvia claro
val EcoOnSecondaryContainerLight = Color(0xFF0D1F12)

val EcoTertiaryLight = Color(0xFF3B6470)          // Azul petróleo / agua limpia
val EcoOnTertiaryLight = Color(0xFFFFFFFF)
val EcoTertiaryContainerLight = Color(0xFFBFE9F8) // Contenedor turquesa suave
val EcoOnTertiaryContainerLight = Color(0xFF001F27)

val EcoErrorLight = Color(0xFFBA1A1A)             // Error semántico
val EcoOnErrorLight = Color(0xFFFFFFFF)
val EcoErrorContainerLight = Color(0xFFFFDAD6)
val EcoOnErrorContainerLight = Color(0xFF410002)

val EcoBackgroundLight = Color(0xFFF7FBF4)        // Fondo blanco fresco orgánico
val EcoOnBackgroundLight = Color(0xFF191D1A)      // Texto oscuro de alto contraste
val EcoSurfaceLight = Color(0xFFF7FBF4)           // Superficie principal
val EcoOnSurfaceLight = Color(0xFF191D1A)
val EcoSurfaceVariantLight = Color(0xFFDDE5DC)    // Variante para chips y tarjetas suaves
val EcoOnSurfaceVariantLight = Color(0xFF414942)  // Texto secundario / subtítulos

val EcoSurfaceContainerLowestLight = Color(0xFFFFFFFF)
val EcoSurfaceContainerLowLight = Color(0xFFF1F5EE)
val EcoSurfaceContainerLight = Color(0xFFEBEFE8)
val EcoSurfaceContainerHighLight = Color(0xFFE5EAE2)
val EcoSurfaceContainerHighestLight = Color(0xFFDFE4DC)

val EcoOutlineLight = Color(0xFF717972)           // Borde acentuado
val EcoOutlineVariantLight = Color(0xFFC1C9C0)    // Borde suave para divisores y campos

// ==========================================
// PALETA MODO OSCURO (Dark Theme - Eco Night)
// ==========================================
val EcoPrimaryDark = Color(0xFF8BD8A5)            // Menta luminosa para alto contraste en negro
val EcoOnPrimaryDark = Color(0xFF00391F)
val EcoPrimaryContainerDark = Color(0xFF00522E)
val EcoOnPrimaryContainerDark = Color(0xFFA6F5C0)

val EcoSecondaryDark = Color(0xFFB6CCB8)          // Salvia luminoso
val EcoOnSecondaryDark = Color(0xFF223526)
val EcoSecondaryContainerDark = Color(0xFF384B3B)
val EcoOnSecondaryContainerDark = Color(0xFFD2E8D3)

val EcoTertiaryDark = Color(0xFFA3CDDC)           // Turquesa luminoso
val EcoOnTertiaryDark = Color(0xFF023641)
val EcoTertiaryContainerDark = Color(0xFF214C58)
val EcoOnTertiaryContainerDark = Color(0xFFBFE9F8)

val EcoErrorDark = Color(0xFFFFB4AB)
val EcoOnErrorDark = Color(0xFF690005)
val EcoErrorContainerDark = Color(0xFF93000A)
val EcoOnErrorContainerDark = Color(0xFFFFDAD6)

val EcoBackgroundDark = Color(0xFF101412)         // Carbón vegetal orgánico
val EcoOnBackgroundDark = Color(0xFFE0E4DE)
val EcoSurfaceDark = Color(0xFF101412)
val EcoOnSurfaceDark = Color(0xFFE0E4DE)
val EcoSurfaceVariantDark = Color(0xFF414942)
val EcoOnSurfaceVariantDark = Color(0xFFC1C9C0)

val EcoSurfaceContainerLowestDark = Color(0xFF0B0F0D)
val EcoSurfaceContainerLowDark = Color(0xFF181D1A)
val EcoSurfaceContainerDark = Color(0xFF1D211E)
val EcoSurfaceContainerHighDark = Color(0xFF272B28)
val EcoSurfaceContainerHighestDark = Color(0xFF323633)

val EcoOutlineDark = Color(0xFF8B938B)
val EcoOutlineVariantDark = Color(0xFF414942)