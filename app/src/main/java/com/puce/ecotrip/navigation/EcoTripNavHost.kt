package com.puce.ecotrip.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.puce.ecotrip.ui.EcoTripViewModel
import com.puce.ecotrip.ui.screens.FormularioScreen
import com.puce.ecotrip.ui.screens.ResumenScreen

/**
 * Orquestador principal del grafo de navegación declarativo y fuertemente tipado.
 * Implementa Type-Safety con Navigation Compose 2.8+ / 2.9+, validando destinos en compilación.
 * Garantiza la higiene del Back Stack mediante popUpTo y la bandera launchSingleTop.
 */
@Composable
fun EcoTripNavHost(
    viewModel: EcoTripViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Rutas.Formulario,
        modifier = modifier
    ) {
        // --- Destino 1: Formulario de Planificación ---
        composable<Rutas.Formulario> {
            FormularioScreen(
                uiState = uiState,
                onNombreChange = viewModel::onNombreChange,
                onDestinoChange = viewModel::onDestinoChange,
                onDiasChange = viewModel::onDiasChange,
                onTransporteChange = viewModel::onTipoTransporteChange,
                onHuellaChange = viewModel::onBajaHuellaChange,
                onGrupalChange = viewModel::onViajeGrupalChange,
                onNavegarResumen = {
                    val rutaResumen = viewModel.construirRutaResumen()
                    navController.navigate(rutaResumen) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // --- Destino 2: Resumen de Ruta Tipado ---
        composable<Rutas.Resumen> { backStackEntry ->
            // Deserialización automática y segura del contrato fuertemente tipado
            val resumenArgs = backStackEntry.toRoute<Rutas.Resumen>()

            ResumenScreen(
                resumen = resumenArgs,
                onVolver = {
                    // Higiene del Back Stack: evita instancias duplicadas de la pantalla anterior
                    navController.navigate(Rutas.Formulario) {
                        popUpTo(Rutas.Formulario) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
