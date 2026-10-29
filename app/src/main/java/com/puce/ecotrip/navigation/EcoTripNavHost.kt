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
 * Orquestador principal de la navegación de EcoTrip.
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
        // Pantalla inicial: configuración del viaje.
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
                    val rutaResumen =
                        viewModel.construirRutaResumen()

                    navController.navigate(rutaResumen) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // Pantalla de resumen: recupera los argumentos tipados.
        composable<Rutas.Resumen> { backStackEntry ->
            val resumenArgs =
                backStackEntry.toRoute<Rutas.Resumen>()

            ResumenScreen(
                resumen = resumenArgs,
                onVolver = {
                    navController.navigate(Rutas.Formulario) {
                        popUpTo<Rutas.Formulario> {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
