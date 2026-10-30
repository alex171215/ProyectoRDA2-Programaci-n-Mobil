package com.puce.ecotrip.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.puce.ecotrip.data.PreferenciasRepository
import com.puce.ecotrip.data.model.PreferenciaViaje
import com.puce.ecotrip.data.model.TipoTransporte
import com.puce.ecotrip.navigation.Rutas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Estado reactivo completo de la interfaz de EcoTrip.
 * Modela la pantalla como una función pura del estado (UI = f(State)).
 */
data class EcoTripUiState(
    val nombreViajero: String = "",
    val destino: String = "",
    val diasTexto: String = "1",
    val diasDuracion: Int = 1,
    val tipoTransporte: TipoTransporte = TipoTransporte.TREN,
    val bajaHuellaCarbono: Boolean = true,
    val esViajeGrupal: Boolean = false,
    val esFormularioValido: Boolean = false
)

/**
 * ViewModel central (Single Source of Truth) para EcoTrip 2026.
 * Implementa Resiliencia Multinivel:
 * 1. Memoria: Flujos reactivos para recomposiciones y rotación.
 * 2. Proceso: SavedStateHandle para blindaje contra Process Death (muerte por falta de RAM).
 * 3. Disco: DataStore a través de PreferenciasRepository para persistencia definitiva tras cierre.
 */
class EcoTripViewModel(
    private val stateHandle: SavedStateHandle,
    private val repository: PreferenciasRepository
) : ViewModel() {

    companion object {
        private const val KEY_DESTINO = "saved_destino"
        private const val KEY_DIAS_TEXTO = "saved_dias_texto"
        private const val KEY_VIAJE_GRUPAL = "saved_viaje_grupal"

        fun provideFactory(
            repository: PreferenciasRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                throw UnsupportedOperationException(
                    "Utilice la sobrecarga con SavedStateHandle o el ViewModelProvider por defecto de Jetpack Compose"
                )
            }
        }
    }

    // --- Persistencia en PROCESO (SavedStateHandle ante Process Death) ---
    private val destinoFlow: StateFlow<String> = stateHandle.getStateFlow(KEY_DESTINO, "")
    private val diasTextoFlow: StateFlow<String> = stateHandle.getStateFlow(KEY_DIAS_TEXTO, "1")
    private val viajeGrupalFlow: StateFlow<Boolean> = stateHandle.getStateFlow(KEY_VIAJE_GRUPAL, false)

    // --- Persistencia en MEMORIA VIVA (Tipo de transporte efímero) ---
    private val tipoTransporteFlow = MutableStateFlow(TipoTransporte.TREN)

    // --- Persistencia en DISCO (DataStore asíncrono permanente) ---
    private val nombreDiscoFlow: StateFlow<String> = repository.nombreViajeroFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    private val bajaHuellaDiscoFlow: StateFlow<Boolean> = repository.bajaHuellaFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )

    // Combina los estados de SavedStateHandle (Proceso)
    private data class SavedStateSnapshot(
        val destino: String,
        val diasTexto: String,
        val esGrupal: Boolean
    )

    private val savedStateSnapshotFlow = combine(
        destinoFlow,
        diasTextoFlow,
        viajeGrupalFlow
    ) { destino, diasTexto, esGrupal ->
        SavedStateSnapshot(destino, diasTexto, esGrupal)
    }

    /**
     * Estado consolidado expuesto a la UI siguiendo el Flujo Unidireccional de Datos (UDF).
     */
    val uiState: StateFlow<EcoTripUiState> = combine(
        nombreDiscoFlow,
        bajaHuellaDiscoFlow,
        tipoTransporteFlow,
        savedStateSnapshotFlow
    ) { nombre, bajaHuella, transporte, savedState ->
        // Sanitización defensiva de entrada numérica en tiempo real
        val diasNumericos = savedState.diasTexto.toIntOrNull() ?: 1
        val esValido = nombre.isNotBlank() && savedState.destino.isNotBlank() && diasNumericos > 0

        EcoTripUiState(
            nombreViajero = nombre,
            destino = savedState.destino,
            diasTexto = savedState.diasTexto,
            diasDuracion = diasNumericos,
            tipoTransporte = transporte,
            bajaHuellaCarbono = bajaHuella,
            esViajeGrupal = savedState.esGrupal,
            esFormularioValido = esValido
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EcoTripUiState()
    )

    // --- Eventos de mutación que suben desde la UI (UDF: Events Up) ---

    fun onNombreChange(nuevoNombre: String) {
        viewModelScope.launch {
            repository.guardarNombreViajero(nuevoNombre)
        }
    }

    fun onDestinoChange(nuevoDestino: String) {
        stateHandle[KEY_DESTINO] = nuevoDestino
    }

    /**
     * Sanitización estricta: sólo acepta dígitos y usa toIntOrNull() para blindar contra crashes.
     */
    fun onDiasChange(nuevoInput: String) {
        val filtrado = nuevoInput.filter { it.isDigit() }
        stateHandle[KEY_DIAS_TEXTO] = filtrado
    }

    fun onTipoTransporteChange(nuevoTipo: TipoTransporte) {
        tipoTransporteFlow.value = nuevoTipo
    }

    fun onBajaHuellaChange(nuevaPreferencia: Boolean) {
        viewModelScope.launch {
            repository.guardarBajaHuella(nuevaPreferencia)
        }
    }

    fun onViajeGrupalChange(esGrupal: Boolean) {
        stateHandle[KEY_VIAJE_GRUPAL] = esGrupal
    }

    /**
     * Construye de manera segura el destino de navegación fuertemente tipado.
     */
    fun construirRutaResumen(): Rutas.Resumen {
        val actual = uiState.value
        return Rutas.Resumen(
            destino = actual.destino.trim(),
            diasDuracion = actual.diasDuracion,
            tipoTransporte = actual.tipoTransporte.etiqueta,
            esBajaHuella = actual.bajaHuellaCarbono,
            nombreViajero = actual.nombreViajero.trim().ifBlank { "Viajero Anónimo" },
            esViajeGrupal = actual.esViajeGrupal
        )
    }

    /**
     * Genera la data class inmutable del modelo de dominio.
     */
    fun obtenerPreferenciaViaje(): PreferenciaViaje {
        val actual = uiState.value
        return PreferenciaViaje(
            nombreViajero = actual.nombreViajero,
            tipoTransporte = actual.tipoTransporte,
            bajaHuellaCarbono = actual.bajaHuellaCarbono
        )
    }
}
