package com.puce.ecotrip.ui.screens // Paquete de pantallas (Rol 2)

import androidx.compose.foundation.background // Fondo de un elemento
import androidx.compose.foundation.horizontalScroll // Scroll horizontal (fila de chips)
import androidx.compose.foundation.layout.Arrangement // Espaciado entre hijos
import androidx.compose.foundation.layout.Box // Contenedor que superpone/centra
import androidx.compose.foundation.layout.Column // Hijos en vertical
import androidx.compose.foundation.layout.Row // Hijos en horizontal
import androidx.compose.foundation.layout.Spacer // Espacio vacío
import androidx.compose.foundation.layout.fillMaxSize // Ocupar todo
import androidx.compose.foundation.layout.fillMaxWidth // Ocupar todo el ancho
import androidx.compose.foundation.layout.height // Alto fijo
import androidx.compose.foundation.layout.padding // Márgenes
import androidx.compose.foundation.layout.size // Ancho y alto fijos
import androidx.compose.foundation.layout.widthIn // Ancho máximo
import androidx.compose.foundation.rememberScrollState // Posición del scroll (estado de UI, permitido)
import androidx.compose.foundation.shape.CircleShape // Forma circular / píldora
import androidx.compose.foundation.shape.RoundedCornerShape // Esquinas redondeadas
import androidx.compose.foundation.text.KeyboardOptions // Tipo de teclado
import androidx.compose.foundation.verticalScroll // Scroll vertical
import androidx.compose.material.icons.Icons // Catálogo de íconos
import androidx.compose.material.icons.automirrored.filled.Send // Ícono enviar (se voltea en RTL)
import androidx.compose.material.icons.filled.DateRange // Ícono calendario
import androidx.compose.material.icons.filled.Favorite // Ícono corazón (huella)
import androidx.compose.material.icons.filled.LocationOn // Ícono ubicación
import androidx.compose.material.icons.filled.Person // Ícono persona
import androidx.compose.material.icons.filled.Share // Ícono grupo
import androidx.compose.material3.Card // Tarjeta
import androidx.compose.material3.CardDefaults // Colores de tarjeta
import androidx.compose.material3.ExperimentalMaterial3Api // TopAppBar es experimental
import androidx.compose.material3.ExtendedFloatingActionButton // FAB con texto (el botón píldora)
import androidx.compose.material3.FabPosition // Posición del FAB en el Scaffold
import androidx.compose.material3.FilterChip // Chip seleccionable
import androidx.compose.material3.FilterChipDefaults // Colores/borde del chip
import androidx.compose.material3.HorizontalDivider // Línea separadora
import androidx.compose.material3.Icon // Dibuja un ícono
import androidx.compose.material3.MaterialTheme // Colores y tipografía del tema (Material You)
import androidx.compose.material3.OutlinedIconButton // Botón circular con borde (− / +)
import androidx.compose.material3.OutlinedTextField // Campo de texto con borde
import androidx.compose.material3.OutlinedTextFieldDefaults // Colores del campo
import androidx.compose.material3.Scaffold // Estructura de pantalla M3
import androidx.compose.material3.Switch // Interruptor
import androidx.compose.material3.Text // Texto
import androidx.compose.material3.TopAppBar // Barra superior
import androidx.compose.material3.TopAppBarDefaults // Colores de la barra
import androidx.compose.runtime.Composable // Marca funciones que dibujan UI
import androidx.compose.ui.Alignment // Alineaciones
import androidx.compose.ui.Modifier // Modificadores de layout
import androidx.compose.ui.draw.clip // Recorta con una forma
import androidx.compose.ui.graphics.vector.ImageVector // Tipo de los íconos
import androidx.compose.ui.semantics.disabled // Marca "deshabilitado" para accesibilidad
import androidx.compose.ui.semantics.semantics // Bloque de semántica (accesibilidad)
import androidx.compose.ui.text.font.FontWeight // Grosor de letra
import androidx.compose.ui.text.input.ImeAction // Botón de acción del teclado
import androidx.compose.ui.text.input.KeyboardType // Teclado numérico/texto
import androidx.compose.ui.text.style.TextOverflow // Qué hacer si el texto no cabe
import androidx.compose.ui.tooling.preview.Preview // Vista previa en Android Studio
import androidx.compose.ui.unit.dp // Unidad de medida
import com.puce.ecotrip.data.model.TipoTransporte // Enum de transportes
import com.puce.ecotrip.ui.EcoTripUiState // Estado que llega del ViewModel
import com.puce.ecotrip.ui.theme.EcoTripTheme // Tema para las previews

/**
 * Pantalla 1 · Formulario (Rol 2). Estilo inspirado en una plantilla de Figma
 * (saludo + avatar, campos redondeados, chips, botón píldora), pero con colores
 * 100 % de MaterialTheme.colorScheme para respetar Material You.
 *
 * STATE HOISTING: no guarda datos propios. El estado BAJA en [uiState] y
 * cada interacción SUBE con una lambda hacia el ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class) // Necesario por TopAppBar
@Composable // Dibuja UI
fun FormularioScreen( // Misma firma de siempre: el NavHost no cambia
    uiState: EcoTripUiState, // Todo lo que se muestra
    onNombreChange: (String) -> Unit, // Evento: cambió el nombre
    onDestinoChange: (String) -> Unit, // Evento: cambió el destino
    onDiasChange: (String) -> Unit, // Evento: cambiaron los días
    onTransporteChange: (TipoTransporte) -> Unit, // Evento: eligió transporte
    onHuellaChange: (Boolean) -> Unit, // Evento: switch de huella
    onGrupalChange: (Boolean) -> Unit, // Evento: switch grupal
    onNavegarResumen: () -> Unit, // Evento: quiere ir al resumen
    modifier: Modifier = Modifier // Ajustes opcionales del llamador
) {
    Scaffold( // Estructura base
        modifier = modifier.fillMaxSize(), // Toda la pantalla
        containerColor = MaterialTheme.colorScheme.surface, // Fondo claro, como el diseño
        topBar = { EncabezadoSaludo(nombre = uiState.nombreViajero) }, // Saludo + avatar
        floatingActionButton = { // Botón píldora inferior
            BotonVerResumen( // Siempre visible; se "apaga" si falta algo
                habilitado = uiState.esFormularioValido, // Lo decide el ViewModel
                dias = uiState.diasDuracion, // Para el texto "| 9 días"
                onClick = onNavegarResumen // Avisa hacia arriba
            )
        },
        floatingActionButtonPosition = FabPosition.Center // Centrado y ancho, como "Add to Cart"
    ) { innerPadding -> // Espacio ocupado por la barra superior
        Box( // Centra el contenido en pantallas anchas
            modifier = Modifier.padding(innerPadding).fillMaxSize(), // Respeta la barra
            contentAlignment = Alignment.TopCenter // Arriba y al centro
        ) {
            Column( // Contenido principal
                modifier = Modifier
                    .widthIn(max = 640.dp) // En horizontal no se estira
                    .fillMaxWidth() // Usa el ancho disponible
                    .verticalScroll(rememberScrollState()) // Permite bajar (clave al rotar)
                    .padding(horizontal = 20.dp, vertical = 8.dp), // Márgenes laterales
                verticalArrangement = Arrangement.spacedBy(16.dp) // Aire entre bloques
            ) {
                Text( // Título grande en negrita
                    text = "Planifica tu ruta", // Texto
                    style = MaterialTheme.typography.headlineSmall, // Tamaño
                    fontWeight = FontWeight.Bold // Negrita, como el diseño
                )
                Text( // Subtítulo gris
                    text = "Completa tus datos y elige cómo moverte.", // Texto
                    style = MaterialTheme.typography.bodyMedium, // Tamaño
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Gris semántico
                )

                CampoRedondeado( // Nombre (se guarda en disco)
                    value = uiState.nombreViajero, // Valor que BAJA
                    onValueChange = onNombreChange, // Cambio que SUBE
                    label = "Nombre del viajero", // Etiqueta
                    icono = Icons.Default.Person // Ícono
                )
                CampoRedondeado( // Destino (SavedStateHandle)
                    value = uiState.destino, // Valor que BAJA
                    onValueChange = onDestinoChange, // Cambio que SUBE
                    label = "¿A dónde vas?", // Etiqueta estilo buscador
                    icono = Icons.Default.LocationOn // Ícono
                )
                SelectorDias( // Días con botones − / +
                    diasTexto = uiState.diasTexto, // Texto crudo
                    onDiasChange = onDiasChange // Cambio que SUBE
                )

                TituloSeccion("Transporte ecológico") // Encabezado de sección
                ChipsTransporte( // Fila de chips
                    seleccionado = uiState.tipoTransporte, // Cuál está elegido
                    onTransporteChange = onTransporteChange // Cambio que SUBE
                )

                TituloSeccion("Preferencias") // Encabezado de sección
                TarjetaPreferencias( // Los dos switches
                    bajaHuella = uiState.bajaHuellaCarbono, // Valor que BAJA
                    esGrupal = uiState.esViajeGrupal, // Valor que BAJA
                    onHuellaChange = onHuellaChange, // Cambio que SUBE
                    onGrupalChange = onGrupalChange // Cambio que SUBE
                )

                val faltantes = buildList { // Lista de lo que falta (solo para mostrar)
                    if (uiState.nombreViajero.isBlank()) add("nombre") // Falta nombre
                    if (uiState.destino.isBlank()) add("destino") // Falta destino
                    if ((uiState.diasTexto.toIntOrNull() ?: 0) <= 0) add("días") // Días vacíos o 0
                }
                if (faltantes.isNotEmpty()) { // Solo si falta algo
                    Text( // Pista con plantilla de cadena
                        text = "Te falta completar: ${faltantes.joinToString(", ")}", // ej. "nombre, días"
                        style = MaterialTheme.typography.bodySmall, // Pequeño
                        color = MaterialTheme.colorScheme.onSurfaceVariant // Gris semántico
                    )
                }

                Spacer(modifier = Modifier.height(96.dp)) // Para que el botón píldora no tape nada
            }
        }
    }
}

/** Barra superior estilo "Hola, bienvenido 👋" + avatar con la inicial. */
@OptIn(ExperimentalMaterial3Api::class) // TopAppBar experimental
@Composable // Dibuja UI
private fun EncabezadoSaludo(nombre: String) { // Recibe el nombre ya escrito
    val inicial = nombre.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "E" // ?. y ?: : sin nombre -> "E"
    TopAppBar( // Sigue siendo la TopAppBar que pide el examen
        title = { // Ranura del título
            Column { // Dos líneas
                Text( // Saludo pequeño
                    text = "Hola, bienvenido 👋", // Texto
                    style = MaterialTheme.typography.bodySmall, // Pequeño
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Gris
                )
                Text( // Nombre en negrita (se actualiza mientras escribes)
                    text = nombre.trim().ifBlank { "Viajero EcoTrip" }, // Valor por defecto si está vacío
                    style = MaterialTheme.typography.titleLarge, // Grande
                    fontWeight = FontWeight.Bold, // Negrita
                    maxLines = 1, // Una sola línea
                    overflow = TextOverflow.Ellipsis // "..." si no cabe
                )
            }
        },
        actions = { // Ranura derecha
            Box( // Avatar circular
                modifier = Modifier
                    .padding(end = 16.dp) // Separación del borde
                    .size(42.dp) // Tamaño del círculo
                    .clip(CircleShape) // Lo hace redondo
                    .background(MaterialTheme.colorScheme.primaryContainer), // Color dinámico
                contentAlignment = Alignment.Center // Inicial al centro
            ) {
                Text( // Letra del avatar
                    text = inicial, // Ej. "A"
                    style = MaterialTheme.typography.titleMedium, // Tamaño
                    fontWeight = FontWeight.Bold, // Negrita
                    color = MaterialTheme.colorScheme.onPrimaryContainer // Contraste correcto
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors( // Barra limpia, sin bloque de color
            containerColor = MaterialTheme.colorScheme.surface, // Mismo fondo que la pantalla
            titleContentColor = MaterialTheme.colorScheme.onSurface // Texto oscuro
        )
    )
}

/** Campo de texto redondeado y de borde suave (como la barra de búsqueda del diseño). */
@Composable // Dibuja UI
private fun CampoRedondeado( // Reutilizable para nombre, destino y días
    value: String, // Texto actual
    onValueChange: (String) -> Unit, // Avisa cada tecla
    label: String, // Etiqueta
    icono: ImageVector, // Ícono a la izquierda
    modifier: Modifier = Modifier, // Ajustes opcionales
    keyboardType: KeyboardType = KeyboardType.Text, // Teclado por defecto
    imeAction: ImeAction = ImeAction.Next, // Botón "siguiente" del teclado
    isError: Boolean = false // Borde rojo si hay error
) {
    OutlinedTextField( // Campo con borde
        value = value, // Valor que BAJA
        onValueChange = onValueChange, // Cambio que SUBE
        modifier = modifier.fillMaxWidth(), // Ancho completo (o el que dé el padre)
        label = { Text(label) }, // Etiqueta flotante
        leadingIcon = { Icon(icono, contentDescription = null) }, // Ícono decorativo
        singleLine = true, // Una línea
        isError = isError, // Estado de error
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction), // Teclado
        shape = RoundedCornerShape(16.dp), // Esquinas redondeadas
        colors = OutlinedTextFieldDefaults.colors( // Colores semánticos
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant, // Borde suave
            focusedBorderColor = MaterialTheme.colorScheme.primary, // Borde al escribir
            unfocusedContainerColor = MaterialTheme.colorScheme.surface, // Fondo
            focusedContainerColor = MaterialTheme.colorScheme.surface // Fondo al escribir
        )
    )
}

/** Días: campo numérico + botones − / + (como el selector de cantidad del diseño). */
@Composable // Dibuja UI
private fun SelectorDias(
    diasTexto: String, // Texto actual (puede estar vacío)
    onDiasChange: (String) -> Unit // Avisa el nuevo texto
) {
    val dias = diasTexto.toIntOrNull() ?: 0 // Conversión segura: "" -> 0, nunca crash
    val invalido = diasTexto.isNotEmpty() && dias <= 0 // Escribió algo pero es 0

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) { // Fila + posible error
        Row( // Campo y botones en línea
            verticalAlignment = Alignment.CenterVertically, // Centrados verticalmente
            horizontalArrangement = Arrangement.spacedBy(10.dp) // Espacio entre ellos
        ) {
            CampoRedondeado( // Campo de días
                value = diasTexto, // Valor que BAJA
                onValueChange = onDiasChange, // Cambio que SUBE (el ViewModel filtra dígitos)
                label = "Duración (días)", // Etiqueta
                icono = Icons.Default.DateRange, // Ícono
                modifier = Modifier.weight(1f), // Ocupa el espacio sobrante
                keyboardType = KeyboardType.Number, // Teclado numérico
                imeAction = ImeAction.Done, // Botón "listo"
                isError = invalido // Rojo si es 0
            )
            BotonCircular(simbolo = "−") { // Restar un día (mínimo 1)
                onDiasChange((dias - 1).coerceAtLeast(1).toString()) // Sube el nuevo valor como texto
            }
            BotonCircular(simbolo = "+") { // Sumar un día
                onDiasChange((dias + 1).toString()) // Sube el nuevo valor como texto
            }
        }
        if (invalido) { // Mensaje de error
            Text( // Texto rojo
                text = "Ingresa al menos 1 día", // Mensaje
                style = MaterialTheme.typography.bodySmall, // Pequeño
                color = MaterialTheme.colorScheme.error // Rojo semántico
            )
        }
    }
}

/** Botón redondo con borde para − y +. */
@Composable // Dibuja UI
private fun BotonCircular(simbolo: String, onClick: () -> Unit) { // Símbolo y acción
    OutlinedIconButton( // Botón circular con borde
        onClick = onClick, // Acción
        modifier = Modifier.size(48.dp) // Área táctil mínima recomendada
    ) {
        Text(simbolo, style = MaterialTheme.typography.titleLarge) // "−" o "+"
    }
}

/** Título de sección en negrita. */
@Composable // Dibuja UI
private fun TituloSeccion(texto: String) { // Texto del título
    Text( // Título
        text = texto, // Contenido
        style = MaterialTheme.typography.titleMedium, // Tamaño
        fontWeight = FontWeight.Bold, // Negrita
        modifier = Modifier.padding(top = 4.dp) // Pequeña separación arriba
    )
}

/** Chips de transporte con scroll horizontal (como "All Items / Dress / T-Shirt"). */
@Composable // Dibuja UI
private fun ChipsTransporte(
    seleccionado: TipoTransporte, // Elegido actualmente
    onTransporteChange: (TipoTransporte) -> Unit // Avisa la elección
) {
    Row( // Fila deslizable
        modifier = Modifier
            .fillMaxWidth() // Ancho completo
            .horizontalScroll(rememberScrollState()), // Se desliza si no caben
        horizontalArrangement = Arrangement.spacedBy(10.dp) // Espacio entre chips
    ) {
        TipoTransporte.entries.forEach { transporte -> // Un chip por cada opción del enum
            val elegido = transporte == seleccionado // ¿Es el seleccionado?
            FilterChip( // Chip seleccionable
                selected = elegido, // Estado visual
                onClick = { onTransporteChange(transporte) }, // Cambio que SUBE
                label = { Text("${emojiDe(transporte)}  ${transporte.etiqueta}") }, // Plantilla: "🚲  Bicicleta"
                modifier = Modifier.height(42.dp), // Alto cómodo
                shape = RoundedCornerShape(12.dp), // Esquinas como el diseño
                colors = FilterChipDefaults.filterChipColors( // Relleno al elegir
                    selectedContainerColor = MaterialTheme.colorScheme.primary, // Fondo dinámico
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary // Texto con contraste
                ),
                border = FilterChipDefaults.filterChipBorder( // Borde suave
                    enabled = true, // Siempre habilitado
                    selected = elegido, // Estado
                    borderColor = MaterialTheme.colorScheme.outlineVariant, // Borde sin elegir
                    selectedBorderColor = MaterialTheme.colorScheme.primary // Borde elegido
                )
            )
        }
    }
}

/** Emoji para cada transporte (when exhaustivo: si se agrega uno al enum, no compila hasta cubrirlo). */
private fun emojiDe(transporte: TipoTransporte): String = when (transporte) { // when como expresión
    TipoTransporte.TREN -> "🚆" // Tren
    TipoTransporte.BICICLETA -> "🚲" // Bicicleta
    TipoTransporte.VEHICULO_ELECTRICO -> "⚡" // Eléctrico
}

/** Tarjeta redondeada con los dos interruptores. */
@Composable // Dibuja UI
private fun TarjetaPreferencias(
    bajaHuella: Boolean, // Valor actual
    esGrupal: Boolean, // Valor actual
    onHuellaChange: (Boolean) -> Unit, // Avisa cambio
    onGrupalChange: (Boolean) -> Unit // Avisa cambio
) {
    Card( // Tarjeta
        modifier = Modifier.fillMaxWidth(), // Ancho completo
        shape = RoundedCornerShape(20.dp), // Muy redondeada, como el diseño
        colors = CardDefaults.cardColors( // Colores semánticos
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow // Gris muy suave
        )
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) { // Margen interno
            FilaSwitch( // Huella
                icono = Icons.Default.Favorite, // Ícono
                titulo = "Baja huella de carbono", // Título
                subtitulo = "Solo rutas sostenibles", // Explicación
                checked = bajaHuella, // Valor que BAJA
                onCheckedChange = onHuellaChange // Cambio que SUBE
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant) // Separador
            FilaSwitch( // Grupal
                icono = Icons.Default.Share, // Ícono
                titulo = "Viaje grupal", // Título
                subtitulo = "Viajas con más personas", // Explicación
                checked = esGrupal, // Valor que BAJA
                onCheckedChange = onGrupalChange // Cambio que SUBE
            )
        }
    }
}

/** Fila: ícono en cuadrito + textos + switch. */
@Composable // Dibuja UI
private fun FilaSwitch(
    icono: ImageVector, // Ícono
    titulo: String, // Texto principal
    subtitulo: String, // Texto secundario
    checked: Boolean, // Estado del switch
    onCheckedChange: (Boolean) -> Unit // Avisa el cambio
) {
    Row( // Todo en línea
        modifier = Modifier
            .fillMaxWidth() // Ancho completo
            .padding(vertical = 10.dp), // Aire arriba/abajo
        verticalAlignment = Alignment.CenterVertically // Centrado vertical
    ) {
        Box( // Cuadrito del ícono
            modifier = Modifier
                .size(40.dp) // Tamaño
                .clip(RoundedCornerShape(12.dp)) // Esquinas redondeadas
                .background(MaterialTheme.colorScheme.primaryContainer), // Color dinámico
            contentAlignment = Alignment.Center // Ícono al centro
        ) {
            Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer) // Ícono
        }
        Column( // Textos
            modifier = Modifier
                .weight(1f) // Ocupa el espacio del medio
                .padding(horizontal = 12.dp) // Separación
        ) {
            Text(titulo, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium) // Título
            Text( // Subtítulo
                text = subtitulo, // Contenido
                style = MaterialTheme.typography.bodySmall, // Pequeño
                color = MaterialTheme.colorScheme.onSurfaceVariant // Gris
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange) // Valor baja, cambio sube
    }
}

/** Botón píldora ancho ("Add to Cart | $..." del diseño). Es el FloatingActionButton del Scaffold. */
@Composable // Dibuja UI
private fun BotonVerResumen(
    habilitado: Boolean, // ¿Formulario válido?
    dias: Int, // Días para el texto
    onClick: () -> Unit // Navegar
) {
    ExtendedFloatingActionButton( // FAB extendido (cuenta como FloatingActionButton)
        text = { // Texto con plantilla de cadena
            Text(
                if (habilitado) "Ver resumen  |  $dias ${if (dias == 1) "día" else "días"}" // "Ver resumen | 9 días"
                else "Completa el formulario" // Mensaje si falta algo
            )
        },
        icon = { Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null) }, // Ícono enviar
        onClick = { if (habilitado) onClick() }, // Solo navega si es válido
        modifier = Modifier
            .widthIn(max = 600.dp) // No demasiado ancho en horizontal
            .fillMaxWidth() // Ancho completo en vertical
            .padding(horizontal = 20.dp) // Márgenes laterales
            .semantics { if (!habilitado) disabled() }, // Lectores de pantalla: "deshabilitado"
        shape = CircleShape, // Forma de píldora
        containerColor = if (habilitado) MaterialTheme.colorScheme.primary // Color dinámico fuerte
        else MaterialTheme.colorScheme.surfaceVariant, // Gris si está "apagado"
        contentColor = if (habilitado) MaterialTheme.colorScheme.onPrimary // Texto con contraste
        else MaterialTheme.colorScheme.onSurfaceVariant // Texto gris
    )
}

// ---- Previews: posibles porque la pantalla es stateless ----

private val estadoDemo = EcoTripUiState( // Estado falso para la vista previa
    nombreViajero = "Alejandro", // Nombre de prueba
    destino = "Galápagos", // Destino de prueba
    diasTexto = "5", // Días de prueba
    diasDuracion = 5, // Días ya convertidos
    esFormularioValido = true // Botón habilitado
)

@Preview(name = "Vertical", showBackground = true) // Teléfono normal
@Composable // Dibuja UI
private fun FormularioPreview() {
    EcoTripTheme { // Con el tema de la app
        FormularioScreen( // La pantalla con lambdas vacías
            uiState = estadoDemo,
            onNombreChange = {}, onDestinoChange = {}, onDiasChange = {},
            onTransporteChange = {}, onHuellaChange = {}, onGrupalChange = {},
            onNavegarResumen = {}
        )
    }
}

@Preview(name = "Incompleto", showBackground = true) // Cómo se ve si falta algo
@Composable // Dibuja UI
private fun FormularioIncompletoPreview() {
    EcoTripTheme { // Con el tema de la app
        FormularioScreen( // Estado vacío: botón apagado y pista visible
            uiState = EcoTripUiState(diasTexto = ""),
            onNombreChange = {}, onDestinoChange = {}, onDiasChange = {},
            onTransporteChange = {}, onHuellaChange = {}, onGrupalChange = {},
            onNavegarResumen = {}
        )
    }
}

@Preview(name = "Horizontal", showBackground = true, widthDp = 800, heightDp = 360) // Teléfono acostado
@Composable // Dibuja UI
private fun FormularioHorizontalPreview() {
    EcoTripTheme { // Con el tema de la app
        FormularioScreen( // Misma pantalla en horizontal
            uiState = estadoDemo,
            onNombreChange = {}, onDestinoChange = {}, onDiasChange = {},
            onTransporteChange = {}, onHuellaChange = {}, onGrupalChange = {},
            onNavegarResumen = {}
        )
    }
}
