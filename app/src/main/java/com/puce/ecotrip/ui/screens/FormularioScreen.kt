package com.puce.ecotrip.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puce.ecotrip.data.model.TipoTransporte
import com.puce.ecotrip.ui.EcoTripUiState
import com.puce.ecotrip.ui.theme.EcoTripTheme

/**
 * Pantalla 1 · Formulario de configuración del viaje (Rol 2).
 *
 * STATE HOISTING: esta pantalla es completamente *stateless*.
 *  - Estado BAJA: todo lo que se dibuja sale de [uiState] (lo produce EcoTripViewModel).
 *  - Eventos SUBEN: cada interacción solo invoca una lambda; el ViewModel decide qué hacer.
 *
 * Como aquí no existe ningún `remember { mutableStateOf(...) }` con datos del formulario,
 * el texto sobrevive a la rotación (vive en el ViewModel) y a la muerte del proceso
 * (el ViewModel lo respalda en SavedStateHandle / DataStore).
 *
 * Colores: solo roles semánticos de MaterialTheme.colorScheme (compatible con Material You).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    uiState: EcoTripUiState,
    onNombreChange: (String) -> Unit,
    onDestinoChange: (String) -> Unit,
    onDiasChange: (String) -> Unit,
    onTransporteChange: (TipoTransporte) -> Unit,
    onHuellaChange: (Boolean) -> Unit,
    onGrupalChange: (Boolean) -> Unit,
    onNavegarResumen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("EcoTrip 2026") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            // El FAB solo aparece cuando el ViewModel declara el formulario válido.
            if (uiState.esFormularioValido) {
                ExtendedFloatingActionButton(
                    onClick = onNavegarResumen,
                    icon = { Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null) },
                    text = { Text("Ver resumen") },
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    ) { innerPadding ->
        // Box + widthIn: en horizontal el contenido queda centrado y legible, sin estirarse.
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 640.dp)
                    .fillMaxWidth()
                    // El scroll es estado de UI (posición), no dato del formulario: permitido.
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Planifica tu ruta",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                SeccionDatosViajero(
                    uiState = uiState,
                    onNombreChange = onNombreChange,
                    onDestinoChange = onDestinoChange,
                    onDiasChange = onDiasChange
                )

                SeccionTransporte(
                    seleccionado = uiState.tipoTransporte,
                    onTransporteChange = onTransporteChange
                )

                SeccionPreferencias(
                    bajaHuella = uiState.bajaHuellaCarbono,
                    esGrupal = uiState.esViajeGrupal,
                    onHuellaChange = onHuellaChange,
                    onGrupalChange = onGrupalChange
                )

                if (!uiState.esFormularioValido) {
                    Text(
                        text = "Completa nombre, destino y días para continuar.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Espacio para que el FAB no tape el último elemento.
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}

/** Campos de texto: nombre (DataStore), destino y días (SavedStateHandle). */
@Composable
private fun SeccionDatosViajero(
    uiState: EcoTripUiState,
    onNombreChange: (String) -> Unit,
    onDestinoChange: (String) -> Unit,
    onDiasChange: (String) -> Unit
) {
    // Validación visual: días escritos pero no convertibles a un entero positivo.
    val diasInvalidos = uiState.diasTexto.isNotEmpty() &&
            (uiState.diasTexto.toIntOrNull() ?: 0) <= 0

    TarjetaSeccion(titulo = "Datos del viaje") {
        OutlinedTextField(
            value = uiState.nombreViajero,
            onValueChange = onNombreChange,
            label = { Text("Nombre del viajero") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.destino,
            onValueChange = onDestinoChange,
            label = { Text("Destino") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.diasTexto,
            onValueChange = onDiasChange,
            label = { Text("Duración (días)") },
            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            singleLine = true,
            isError = diasInvalidos,
            supportingText = if (diasInvalidos) {
                { Text("Ingresa un número de días mayor a 0") }
            } else null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/** Selector de transporte con RadioButtons: una opción por fila, cabe en vertical y horizontal. */
@Composable
private fun SeccionTransporte(
    seleccionado: TipoTransporte,
    onTransporteChange: (TipoTransporte) -> Unit
) {
    TarjetaSeccion(titulo = "Transporte ecológico") {
        Column(modifier = Modifier.selectableGroup()) {
            TipoTransporte.entries.forEach { transporte ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = transporte == seleccionado,
                            onClick = { onTransporteChange(transporte) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // onClick = null: el clic lo maneja toda la fila (área táctil más grande).
                    RadioButton(selected = transporte == seleccionado, onClick = null)
                    Text(
                        text = transporte.etiqueta,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        }
    }
}

/** Interruptores: baja huella (DataStore) y viaje grupal (SavedStateHandle). */
@Composable
private fun SeccionPreferencias(
    bajaHuella: Boolean,
    esGrupal: Boolean,
    onHuellaChange: (Boolean) -> Unit,
    onGrupalChange: (Boolean) -> Unit
) {
    TarjetaSeccion(titulo = "Preferencias") {
        FilaSwitch(
            icono = Icons.Default.Favorite,
            texto = "Solo rutas de baja huella de carbono",
            checked = bajaHuella,
            onCheckedChange = onHuellaChange
        )
        FilaSwitch(
            icono = Icons.Default.Share,
            texto = "Viaje grupal",
            checked = esGrupal,
            onCheckedChange = onGrupalChange
        )
    }
}

@Composable
private fun FilaSwitch(
    icono: ImageVector,
    texto: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

/** Contenedor reutilizable: Card con título usando roles de color semánticos. */
@Composable
private fun TarjetaSeccion(
    titulo: String,
    contenido: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            contenido()
        }
    }
}

// ---- Previews: posibles porque la pantalla es stateless (se le pasa un estado falso) ----

private val estadoDemo = EcoTripUiState(
    nombreViajero = "Ana",
    destino = "Galápagos",
    diasTexto = "5",
    esFormularioValido = true
)

@Preview(name = "Vertical", showBackground = true)
@Composable
private fun FormularioPreview() {
    EcoTripTheme {
        FormularioScreen(
            uiState = estadoDemo,
            onNombreChange = {}, onDestinoChange = {}, onDiasChange = {},
            onTransporteChange = {}, onHuellaChange = {}, onGrupalChange = {},
            onNavegarResumen = {}
        )
    }
}

@Preview(name = "Horizontal", showBackground = true, widthDp = 800, heightDp = 360)
@Composable
private fun FormularioHorizontalPreview() {
    EcoTripTheme {
        FormularioScreen(
            uiState = estadoDemo,
            onNombreChange = {}, onDestinoChange = {}, onDiasChange = {},
            onTransporteChange = {}, onHuellaChange = {}, onGrupalChange = {},
            onNavegarResumen = {}
        )
    }
}
