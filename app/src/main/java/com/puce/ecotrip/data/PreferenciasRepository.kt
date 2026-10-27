package com.puce.ecotrip.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Extensión de contexto para delegar la instancia singleton de DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ecotrip_preferences")

/**
 * Repositorio de persistencia en disco mediante DataStore Preferences.
 * Garantiza que los datos de configuración global (nombre y preferencia de huella)
 * sobrevivan al cierre total de la aplicación y reinicios del dispositivo.
 */
class PreferenciasRepository(private val context: Context) {

    companion object {
        val KEY_NOMBRE_VIAJERO = stringPreferencesKey("nombre_viajero")
        val KEY_BAJA_HUELLA = booleanPreferencesKey("baja_huella_carbono")
    }

    /**
     * Flujo asíncrono para leer el nombre del viajero guardado en disco.
     */
    val nombreViajeroFlow: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[KEY_NOMBRE_VIAJERO] ?: ""
        }

    /**
     * Flujo asíncrono para leer la preferencia de baja huella de carbono.
     */
    val bajaHuellaFlow: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[KEY_BAJA_HUELLA] ?: true
        }

    /**
     * Guarda el nombre del viajero en disco de forma transaccional y asíncrona.
     */
    suspend fun guardarNombreViajero(nombre: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_NOMBRE_VIAJERO] = nombre
        }
    }

    /**
     * Guarda la preferencia de huella de carbono en disco de forma asíncrona.
     */
    suspend fun guardarBajaHuella(bajaHuella: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_BAJA_HUELLA] = bajaHuella
        }
    }
}
