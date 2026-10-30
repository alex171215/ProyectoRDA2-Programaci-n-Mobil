package com.puce.ecotrip.navigation

import kotlinx.serialization.Serializable

/**
 * Definición de destinos declarativos y fuertemente tipados (Type-Safety).
 * Cumple con el estándar de Android 16 eliminando cadenas de texto nativas y Bundles manuales.
 */
sealed interface Rutas {

    /**
     * Destino del Formulario de planificación y configuración global.
     */
    @Serializable
    data object Formulario : Rutas

    /**
     * Destino del Resumen de Viaje con transferencia segura de argumentos inmutables.
     */
    @Serializable
    data class Resumen(
        val destino: String,
        val diasDuracion: Int,
        val tipoTransporte: String,
        val esBajaHuella: Boolean,
        val nombreViajero: String,
        val esViajeGrupal: Boolean
    ) : Rutas
}
