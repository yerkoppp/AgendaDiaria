package dev.ycosorio.agendadiaria.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AgendaLightBlue,
    secondary = AgendaLightOrange,
    tertiary = AgendaBlue,
    background = Color.Black,
    surface = DarkSurface,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = AgendaOrange,
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Color.Gray
)

private val LightColorScheme = lightColorScheme(
    primary = AgendaBlue,
    secondary = AgendaOrange,
    tertiary = AgendaLightBlue,
    background = LightSurface,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = AgendaLightOrange,
    onBackground = OnSurfaceText,
    onSurface = OnSurfaceText,
    onSurfaceVariant = Color.Gray
)

@Composable
fun AgendaDiariaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Desactivado para usar nuestra paleta personalizada
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}