package com.puce.ecotrip.data.model

import kotlinx.serialization.Serializable

/**
 * Enumeración que representa los medios de transporte ecológicos disponibles en EcoTrip 2026.
 */
enum class TipoTransporte(val etiqueta: String) {
    TREN("Tren"),
    BICICLETA("Bicicleta"),
    VEHICULO_ELECTRICO("Vehículo Eléctrico")
}

/**
 * Modelo de datos inmutable para las preferencias del usuario.
 * Cumple con el estándar de inmutabilidad (val) y tipado estricto.
 */
@Serializable
data class PreferenciaViaje(
    val nombreViajero: String,
    val tipoTransporte: TipoTransporte = TipoTransporte.TREN,
    val bajaHuellaCarbono: Boolean = true
)
