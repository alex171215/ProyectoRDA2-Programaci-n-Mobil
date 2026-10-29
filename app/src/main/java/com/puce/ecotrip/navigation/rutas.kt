
package com.puce.ecotrip.navigation

import com.puce.ecotrip.data.model.PreferenciaViaje
import kotlinx.serialization.Serializable

/**
 * Define los destinos de navegación de EcoTrip.
 * Todas las rutas están fuertemente tipadas.
 */
object Rutas {

    @Serializable
    data object Formulario

    @Serializable
    data class Resumen(
        val preferenciaViaje: PreferenciaViaje,
        val destino: String,
        val diasDuracion: Int,
        val esViajeGrupal: Boolean
    )
}
