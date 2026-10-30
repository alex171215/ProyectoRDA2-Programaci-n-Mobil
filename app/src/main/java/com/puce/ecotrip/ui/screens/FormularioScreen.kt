package com.puce.ecotrip.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puce.ecotrip.data.model.TipoTransporte
import com.puce.ecotrip.ui.EcoTripUiState
import com.puce.ecotrip.ui.theme.EcoTripTheme

/**
 * Pantalla 1: formulario de configuración del viaje.
 *
 * Es stateless (State Hoisting): todo lo que muestra llega en [uiState] y cada
 * interacción se notifica con una lambda. Las reglas de validación viven en
 * [EcoTripUiState] (EcoTripViewModel), no aquí. Solo usa colores de MaterialTheme (Material You).
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
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = { EncabezadoSaludo(nombre = uiState.nombreViajero) },
        floatingActionButton = {
            BotonVerResumen(
                habilitado = uiState.esFormularioValido,
                dias = uiState.diasDuracion,
                onClick = onNavegarResumen
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 640.dp) // en horizontal el contenido no se estira
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Planifica tu ruta",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Completa tus datos y elige cómo moverte.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                CampoRedondeado(
                    value = uiState.nombreViajero,
                    onValueChange = onNombreChange,
                    label = "Nombre del viajero",
                    icono = Icons.Default.Person
                )
                CampoRedondeado(
                    value = uiState.destino,
                    onValueChange = onDestinoChange,
                    label = "¿A dónde vas?",
                    icono = Icons.Default.LocationOn
                )
                SelectorDias(
                    diasTexto = uiState.diasTexto,
                    dias = uiState.diasDuracion,
                    onDiasChange = onDiasChange
                )

                TituloSeccion("Transporte ecológico")
                ChipsTransporte(
                    seleccionado = uiState.tipoTransporte,
                    onTransporteChange = onTransporteChange
                )

                TituloSeccion("Preferencias")
                TarjetaPreferencias(
                    bajaHuella = uiState.bajaHuellaCarbono,
                    esGrupal = uiState.esViajeGrupal,
                    onHuellaChange = onHuellaChange,
                    onGrupalChange = onGrupalChange
                )

                // Espacio para que el botón inferior no tape el último elemento.
                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EncabezadoSaludo(nombre: String) {
    val inicial = nombre.trim().firstOrNull()?.uppercase() ?: "E"

    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Hola, bienvenido 👋",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = nombre.trim().ifBlank { "Viajero EcoTrip" },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = inicial,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun CampoRedondeado(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = { Icon(icono, contentDescription = null) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            focusedBorderColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun SelectorDias(
    diasTexto: String,
    dias: Int,
    onDiasChange: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CampoRedondeado(
            value = diasTexto,
            onValueChange = onDiasChange,
            label = "Duración (días)",
            icono = Icons.Default.DateRange,
            modifier = Modifier.weight(1f),
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
        // Los botones solo traducen el clic a un nuevo texto; el ViewModel lo valida igual que si se escribiera.
        BotonCircular(simbolo = "−") { onDiasChange((dias - 1).coerceAtLeast(1).toString()) }
        BotonCircular(simbolo = "+") { onDiasChange((dias + 1).toString()) }
    }
}

@Composable
private fun BotonCircular(simbolo: String, onClick: () -> Unit) {
    OutlinedIconButton(onClick = onClick, modifier = Modifier.size(48.dp)) {
        Text(simbolo, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun TituloSeccion(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
private fun ChipsTransporte(
    seleccionado: TipoTransporte,
    onTransporteChange: (TipoTransporte) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TipoTransporte.entries.forEach { transporte ->
            val elegido = transporte == seleccionado
            FilterChip(
                selected = elegido,
                onClick = { onTransporteChange(transporte) },
                label = { Text("${transporte.emoji}  ${transporte.etiqueta}") },
                modifier = Modifier.height(42.dp),
                shape = RoundedCornerShape(12.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = elegido,
                    borderColor = MaterialTheme.colorScheme.outlineVariant,
                    selectedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

/** `when` exhaustivo: si se agrega un transporte al enum, esto no compila hasta cubrirlo. */
private val TipoTransporte.emoji: String
    get() = when (this) {
        TipoTransporte.TREN -> "🚆"
        TipoTransporte.BICICLETA -> "🚲"
        TipoTransporte.VEHICULO_ELECTRICO -> "⚡"
    }

@Composable
private fun TarjetaPreferencias(
    bajaHuella: Boolean,
    esGrupal: Boolean,
    onHuellaChange: (Boolean) -> Unit,
    onGrupalChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            FilaSwitch(
                icono = Icons.Default.Favorite,
                titulo = "Baja huella de carbono",
                subtitulo = "Solo rutas sostenibles",
                checked = bajaHuella,
                onCheckedChange = onHuellaChange
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            FilaSwitch(
                icono = Icons.Default.Share,
                titulo = "Viaje grupal",
                subtitulo = "Viajas con más personas",
                checked = esGrupal,
                onCheckedChange = onGrupalChange
            )
        }
    }
}

@Composable
private fun FilaSwitch(
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(titulo, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Text(
                text = subtitulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

/** Botón principal en forma de píldora; es el FloatingActionButton del Scaffold. */
@Composable
private fun BotonVerResumen(
    habilitado: Boolean,
    dias: Int,
    onClick: () -> Unit
) {
    val texto = if (habilitado) {
        "Ver resumen  |  $dias ${if (dias == 1) "día" else "días"}"
    } else {
        "Completa nombre, destino y días"
    }

    ExtendedFloatingActionButton(
        text = { Text(texto) },
        icon = { Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null) },
        onClick = { if (habilitado) onClick() },
        modifier = Modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = CircleShape,
        containerColor = if (habilitado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (habilitado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    )
}

// ---- Previews: posibles porque la pantalla es stateless ----

@Preview(name = "Completo", showBackground = true)
@Composable
private fun FormularioCompletoPreview() {
    EcoTripTheme {
        FormularioScreen(
            uiState = EcoTripUiState(nombreViajero = "Alejandro", destino = "Galápagos", diasTexto = "5", diasDuracion = 5, esFormularioValido = true),
            onNombreChange = {}, onDestinoChange = {}, onDiasChange = {},
            onTransporteChange = {}, onHuellaChange = {}, onGrupalChange = {},
            onNavegarResumen = {}
        )
    }
}

@Preview(name = "Vacío", showBackground = true)
@Composable
private fun FormularioVacioPreview() {
    EcoTripTheme {
        FormularioScreen(
            uiState = EcoTripUiState(diasTexto = "", diasDuracion = 0),
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
            uiState = EcoTripUiState(nombreViajero = "Alejandro", destino = "Galápagos", diasTexto = "5", diasDuracion = 5, esFormularioValido = true),
            onNombreChange = {}, onDestinoChange = {}, onDiasChange = {},
            onTransporteChange = {}, onHuellaChange = {}, onGrupalChange = {},
            onNavegarResumen = {}
        )
    }
}
