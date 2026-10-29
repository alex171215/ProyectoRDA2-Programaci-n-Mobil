
package com.puce.ecotrip.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.puce.ecotrip.data.model.PreferenciaViaje
import com.puce.ecotrip.data.model.TipoTransporte
import com.puce.ecotrip.navigation.Rutas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FormularioUiState(
    val nombreViajero: String = "",
    val destino: String = "",
    val diasTexto: String = "",
    val tipoTransporte: TipoTransporte = TipoTransporte.TREN,
    val bajaHuellaCarbono: Boolean = true,
    val esViajeGrupal: Boolean = false
) {
    val diasDuracion: Int
        get() = diasTexto.toIntOrNull() ?: 0

    val esFormularioValido: Boolean
        get() = nombreViajero.isNotBlank() &&
                destino.isNotBlank() &&
                diasDuracion > 0
}

class EcoTripViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        FormularioUiState(
            nombreViajero =
                savedStateHandle["nombreViajero"] ?: "",
            destino =
                savedStateHandle["destino"] ?: "",
            diasTexto =
                savedStateHandle["diasTexto"] ?: "",
            tipoTransporte =
                TipoTransporte.valueOf(
                    savedStateHandle["tipoTransporte"]
                        ?: TipoTransporte.TREN.name
                ),
            bajaHuellaCarbono =
                savedStateHandle["bajaHuellaCarbono"] ?: true,
            esViajeGrupal =
                savedStateHandle["esViajeGrupal"] ?: false
        )
    )

    val uiState = _uiState.asStateFlow()

    fun onNombreChange(valor: String) {
        actualizar("nombreViajero", valor) {
            copy(nombreViajero = valor)
        }
    }

    fun onDestinoChange(valor: String) {
        actualizar("destino", valor) {
            copy(destino = valor)
        }
    }

    fun onDiasChange(valor: String) {
        if (valor.all { it.isDigit() }) {
            actualizar("diasTexto", valor) {
                copy(diasTexto = valor)
            }
        }
    }

    fun onTipoTransporteChange(valor: TipoTransporte) {
        actualizar("tipoTransporte", valor.name) {
            copy(tipoTransporte = valor)
        }
    }

    fun onBajaHuellaChange(valor: Boolean) {
        actualizar("bajaHuellaCarbono", valor) {
            copy(bajaHuellaCarbono = valor)
        }
    }

    fun onViajeGrupalChange(valor: Boolean) {
        actualizar("esViajeGrupal", valor) {
            copy(esViajeGrupal = valor)
        }
    }

    private fun actualizar(
        clave: String,
        valorGuardado: Any,
        cambio: FormularioUiState.() -> FormularioUiState
    ) {
        savedStateHandle[clave] = valorGuardado
        _uiState.update(cambio)
    }

    fun construirRutaResumen(): Rutas.Resumen {
        val estado = _uiState.value

        require(estado.esFormularioValido) {
            "Completa el nombre, destino y días válidos."
        }

        return Rutas.Resumen(
            preferenciaViaje = PreferenciaViaje(
                nombreViajero = estado.nombreViajero,
                tipoTransporte = estado.tipoTransporte,
                bajaHuellaCarbono = estado.bajaHuellaCarbono
            ),
            destino = estado.destino,
            diasDuracion = requireNotNull(
                estado.diasTexto.toIntOrNull()
            ),
            esViajeGrupal = estado.esViajeGrupal
        )
    }
}
