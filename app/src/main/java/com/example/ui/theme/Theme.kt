package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ClayCoral,
    secondary = SandalwoodCreamOnDark,
    tertiary = DarkCaramelAccent,
    background = DarkEspressoBg,
    surface = DarkSiennaSurface,
    onPrimary = Color(0xFF1D0E0B),
    onSecondary = SandalwoodCreamOnDark,
    onTertiary = SandalwoodCreamOnDark,
    onBackground = SandalwoodCreamOnDark,
    onSurface = SandalwoodCreamOnDark,
    outline = BorderWarmSienna
)

private val LightColorScheme = lightColorScheme(
    primary = TerracottaCrimson,
    secondary = NeutralMutedText,
    tertiary = AccentTanCard,
    background = NaturalSandalwood,
    surface = SurfaceLinenCard,
    onPrimary = Color.White,
    onSecondary = CocoaInkText,
    onTertiary = CocoaInkText,
    onBackground = CocoaInkText,
    onSurface = CocoaInkText,
    outline = BorderWarmSienna
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
