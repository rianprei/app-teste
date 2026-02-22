package com.maicon.treinoemcasa.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightScheme = lightColorScheme(
    primary = Mint700,
    secondary = Amber500,
    tertiary = Coral500,
    background = SurfaceCream,
    surface = androidx.compose.ui.graphics.Color.White,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = Slate900,
    onBackground = Slate900,
    onSurface = Slate900
)

private val DarkScheme = darkColorScheme(
    primary = Mint500,
    secondary = Amber300,
    tertiary = Coral500,
    background = Slate900,
    surface = Slate700,
    onPrimary = Slate900,
    onSecondary = Slate900,
    onBackground = Slate100,
    onSurface = Slate100
)

@Composable
fun TreinoEmCasaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = AppTypography,
        content = content
    )
}
