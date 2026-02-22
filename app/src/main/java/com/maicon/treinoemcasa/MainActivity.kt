package com.maicon.treinoemcasa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maicon.treinoemcasa.ui.theme.TreinoEmCasaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TreinoEmCasaTheme {
                AppContent()
            }
        }
    }
}

@Composable
private fun AppContent() {
    val appViewModel: AppViewModel = viewModel()
    TreinoApp(viewModel = appViewModel)
}
