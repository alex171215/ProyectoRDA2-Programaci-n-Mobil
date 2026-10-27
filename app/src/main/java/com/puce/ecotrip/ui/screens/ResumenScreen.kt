package com.puce.ecotrip.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import com.puce.ecotrip.navigation.Rutas

/**
 * Pantalla 2: Panel Dinámico de Confirmación de Ruta.
 * Recibe los datos fuertemente tipados a través del contrato @Serializable Rutas.Resumen.
 * Se adapta de inmediato al esquema de colores dinámicos (Material You) del sistema operativo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(
    resumen: Rutas.Resumen,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Itinerario EcoTrip",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar al formulario"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cabecera de éxito
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Ruta Planificada Exitosamente",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = "Resumen tipado recibido mediante navegación segura (@Serializable):",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Tarjeta principal con roles cromáticos semánticos (secondaryContainer)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    FilaDetalle(
                        icono = Icons.Default.Person,
                        titulo = "Viajero Titular",
                        valor = resumen.nombreViajero
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    FilaDetalle(
                        icono = Icons.Default.LocationOn,
                        titulo = "Destino Ecológico",
                        valor = resumen.destino
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    FilaDetalle(
                        icono = Icons.Default.DateRange,
                        titulo = "Duración Estimada",
                        valor = "${resumen.diasDuracion} ${if (resumen.diasDuracion == 1) "día" else "días"}"
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    FilaDetalle(
                        icono = Icons.Default.Place,
                        titulo = "Modo de Transporte",
                        valor = resumen.tipoTransporte
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    FilaDetalle(
                        icono = Icons.Default.Favorite,
                        titulo = "Compromiso Huella de Carbono",
                        valor = if (resumen.esBajaHuella) "Ruta Certificada Baja Huella (-65% CO2)" else "Estándar"
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    FilaDetalle(
                        icono = Icons.Default.Share,
                        titulo = "Modalidad del Viaje",
                        valor = if (resumen.esViajeGrupal) "Viaje Grupal Organizado" else "Viaje Individual / Familiar"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de retorno que dispara la limpieza higiénica del Back Stack
            Button(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Modificar Planificación",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun FilaDetalle(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    titulo: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Column {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}
