package com.puce.ecotrip.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puce.ecotrip.data.model.PreferenciaViaje
import com.puce.ecotrip.data.model.TipoTransporte
import com.puce.ecotrip.navigation.Rutas
import com.puce.ecotrip.ui.theme.EcoTripTheme

/**
 * Pantalla de resumen (Rol 3).
 * Muestra el itinerario final renderizando el modelo PreferenciaViaje.
 * Utiliza colores semánticos de MaterialTheme para soportar Material You
 * y plantillas de cadena idiomáticas de Kotlin para formatear los textos.
 */
@Composable
fun ResumenScreen(
    resumen: Rutas.Resumen,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Convierte el texto recibido por navegación al enum correspondiente
    val transporteEnum = TipoTransporte.entries.find { it.etiqueta.equals(resumen.tipoTransporte, ignoreCase = true) }
        ?: TipoTransporte.TREN

    // Reconstruye el objeto de dominio inmutable con los datos del viaje
    val preferenciaViaje = PreferenciaViaje(
        nombreViajero = resumen.nombreViajero,
        tipoTransporte = transporteEnum,
        bajaHuellaCarbono = resumen.esBajaHuella
    )

    ResumenContenido(
        preferencia = preferenciaViaje,
        destino = resumen.destino,
        diasDuracion = resumen.diasDuracion,
        esViajeGrupal = resumen.esViajeGrupal,
        onVolver = onVolver,
        modifier = modifier
    )
}

/**
 * Sobrecarga desacoplada para renderizar la interfaz recibiendo directamente el modelo.
 * Facilita las pruebas y el uso de vistas previas en Compose.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenContenido(
    preferencia: PreferenciaViaje,
    destino: String,
    diasDuracion: Int,
    esViajeGrupal: Boolean,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Itinerario Confirmado",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "EcoTrip 2026 · Rutas Sostenibles",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar al formulario"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            // Botón inferior para regresar al formulario y modificar datos
            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = "Modificar Planificación",
                        fontWeight = FontWeight.Medium
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null
                    )
                },
                onClick = onVolver,
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 640.dp) // Limita el ancho en pantallas grandes o tablets
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()) // Permite scroll al rotar la pantalla
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tarjeta con saludo al viajero y confirmación de la ruta
                TarjetaBienvenida(
                    nombre = preferencia.nombreViajero,
                    destino = destino
                )

                // Resumen rápido de duración, transporte y huella
                FilaInsignias(
                    dias = diasDuracion,
                    transporte = preferencia.tipoTransporte,
                    bajaHuella = preferencia.bajaHuellaCarbono
                )

                // Desglose de atributos del objeto PreferenciaViaje
                TarjetaEspecificacion(
                    preferencia = preferencia,
                    destino = destino,
                    diasDuracion = diasDuracion,
                    esViajeGrupal = esViajeGrupal
                )

                // Cálculo informativo del ahorro de CO2
                TarjetaImpactoAmbiental(
                    bajaHuella = preferencia.bajaHuellaCarbono,
                    diasDuracion = diasDuracion
                )

                Spacer(modifier = Modifier.height(88.dp)) // Espacio para no tapar contenido con el FAB
            }
        }
    }
}

/**
 * Muestra el avatar con la inicial del viajero y el mensaje de confirmación.
 */
@Composable
private fun TarjetaBienvenida(
    nombre: String,
    destino: String
) {
    val inicial = nombre.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "E"
    val nombreMostrado = nombre.trim().ifBlank { "Viajero EcoTrip" }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Contenedor circular para la inicial del usuario
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = inicial,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "¡Ruta Planificada con Éxito!",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                // Uso de plantillas de cadena para combinar texto dinámico
                Text(
                    text = "Viajero: $nombreMostrado",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Destino asignado: ${destino.ifBlank { "Destino Sostenible" }}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

/**
 * Organiza las tres métricas principales del viaje en tarjetas individuales.
 */
@Composable
private fun FilaInsignias(
    dias: Int,
    transporte: TipoTransporte,
    bajaHuella: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CapsulaMetrica(
            icono = Icons.Default.DateRange,
            titulo = "Duración",
            valor = "$dias ${if (dias == 1) "día" else "días"}",
            modifier = Modifier.weight(1f)
        )
        CapsulaMetrica(
            icono = Icons.Default.Place,
            titulo = "Transporte",
            valor = "${emojiDe(transporte)} ${transporte.etiqueta}",
            modifier = Modifier.weight(1.3f)
        )
        CapsulaMetrica(
            icono = Icons.Default.Favorite,
            titulo = "Huella CO2",
            valor = if (bajaHuella) "−65% CO2" else "Estándar",
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Tarjeta individual pequeña para representar una métrica con ícono y texto.
 */
@Composable
private fun CapsulaMetrica(
    icono: ImageVector,
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Renderiza todos los campos del objeto PreferenciaViaje en filas separadas.
 */
@Composable
private fun TarjetaEspecificacion(
    preferencia: PreferenciaViaje,
    destino: String,
    diasDuracion: Int,
    esViajeGrupal: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Especificaciones del Objeto PreferenciaViaje",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Modelo inmutable renderizado mediante plantillas de cadena idiomáticas.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            FilaDetalleResumen(
                icono = Icons.Default.Person,
                etiqueta = "Titular de la Preferencia",
                valor = "Viajero: ${preferencia.nombreViajero.ifBlank { "Sin registrar" }}"
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            FilaDetalleResumen(
                icono = Icons.Default.LocationOn,
                etiqueta = "Destino Ecológico",
                valor = "Rumbo a $destino (${diasDuracion} ${if (diasDuracion == 1) "día proyectado" else "días proyectados"})"
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            FilaDetalleResumen(
                icono = Icons.Default.Place,
                etiqueta = "Modo de Movilidad Ecológica",
                valor = "${emojiDe(preferencia.tipoTransporte)} Modo seleccionado: ${preferencia.tipoTransporte.etiqueta}"
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            FilaDetalleResumen(
                icono = Icons.Default.Favorite,
                etiqueta = "Compromiso Climático",
                valor = if (preferencia.bajaHuellaCarbono) {
                    "🌱 Certificación Activa: Prioridad de emisión mínima"
                } else {
                    "Ruta sin filtro de huella de carbono"
                }
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            FilaDetalleResumen(
                icono = Icons.Default.Share,
                etiqueta = "Tipo de Expedición",
                valor = if (esViajeGrupal) {
                    "👥 Modalidad Grupal: Acompañamiento compartido"
                } else {
                    "👤 Modalidad Individual: Viaje personal o autónomo"
                }
            )
        }
    }
}

/**
 * Fila reutilizable con ícono destacado y textos en jerarquía.
 */
@Composable
private fun FilaDetalleResumen(
    icono: ImageVector,
    etiqueta: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(22.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Tarjeta informativa que calcula un estimado simple del ahorro de CO2 en base a los días.
 */
@Composable
private fun TarjetaImpactoAmbiental(
    bajaHuella: Boolean,
    diasDuracion: Int
) {
    val ahorroCo2 = diasDuracion * 12 // Estimado simple de ahorro en kilogramos de CO2
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (bajaHuella) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = if (bajaHuella) "🍃" else "💡",
                style = MaterialTheme.typography.headlineMedium
            )
            Column {
                Text(
                    text = if (bajaHuella) "Huella de Carbono Optimizada" else "Sugerencia Ecológica",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (bajaHuella) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (bajaHuella) {
                        "Gracias a tu elección, evitarás emitir ~$ahorroCo2 kg de CO2 a la atmósfera durante tu estadía."
                    } else {
                        "Puedes activar 'Baja Huella' en el formulario para reducir hasta un 65% de emisiones."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = if (bajaHuella) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Retorna el emoji según el medio de transporte seleccionado en el enum.
 */
private fun emojiDe(transporte: TipoTransporte): String = when (transporte) {
    TipoTransporte.TREN -> "🚆"
    TipoTransporte.BICICLETA -> "🚲"
    TipoTransporte.VEHICULO_ELECTRICO -> "⚡"
}

// ==========================================
// PREVIEWS PARA ANDROID STUDIO
// ==========================================

// Objeto de prueba para visualizar el diseño en el editor
private val preferenciaDemo = PreferenciaViaje(
    nombreViajero = "Joselyn Cadena",
    tipoTransporte = TipoTransporte.VEHICULO_ELECTRICO,
    bajaHuellaCarbono = true
)

@Preview(name = "Modo Claro", showBackground = true)
@Composable
private fun ResumenScreenLightPreview() {
    EcoTripTheme(darkTheme = false, dynamicColor = false) {
        ResumenContenido(
            preferencia = preferenciaDemo,
            destino = "Quilotoa Crater Lagoon",
            diasDuracion = 4,
            esViajeGrupal = true,
            onVolver = {}
        )
    }
}

@Preview(name = "Modo Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ResumenScreenDarkPreview() {
    EcoTripTheme(darkTheme = true, dynamicColor = false) {
        ResumenContenido(
            preferencia = preferenciaDemo,
            destino = "Parque Nacional Cotopaxi",
            diasDuracion = 3,
            esViajeGrupal = false,
            onVolver = {}
        )
    }
}

@Preview(name = "Horizontal / Tablet", showBackground = true, widthDp = 800, heightDp = 360)
@Composable
private fun ResumenScreenLandscapePreview() {
    EcoTripTheme(darkTheme = false, dynamicColor = false) {
        ResumenContenido(
            preferencia = preferenciaDemo,
            destino = "Mindo Cloud Forest",
            diasDuracion = 2,
            esViajeGrupal = true,
            onVolver = {}
        )
    }
}
