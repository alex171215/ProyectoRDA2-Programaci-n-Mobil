package com.puce.ecotrip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.puce.ecotrip.ui.theme.EcoTripTheme

/**
 * Punto de entrada único de EcoTrip 2026.
 * El placeholder se reemplaza por el NavHost (EcoTripNavHost) en el commit
 * "feat: type-safe navigation and testing".
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoTripTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(
                        text = "EcoTrip 2026",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
