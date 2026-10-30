package com.puce.ecotrip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.puce.ecotrip.data.PreferenciasRepository
import com.puce.ecotrip.navigation.EcoTripNavHost
import com.puce.ecotrip.ui.EcoTripViewModel
import com.puce.ecotrip.ui.theme.EcoTripTheme

/**
 * Punto de entrada único de EcoTrip 2026.
 * Inicializa el andamiaje Edge-to-Edge y provee el ViewModel con
 * SavedStateHandle y DataStore al grafo de navegación declarativo.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoTripTheme {
                val context = LocalContext.current.applicationContext
                val viewModel: EcoTripViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                            val savedStateHandle = extras.createSavedStateHandle()
                            val repository = PreferenciasRepository(context)
                            return EcoTripViewModel(savedStateHandle, repository) as T
                        }
                    }
                )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EcoTripNavHost(viewModel = viewModel)
                }
            }
        }
    }
}
