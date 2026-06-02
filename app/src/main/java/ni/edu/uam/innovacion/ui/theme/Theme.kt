package ni.edu.uam.innovacion.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = UamTurquoiseAccent,
    onPrimary = UamWhite,
    secondary = UamInnovationYellow,
    onSecondary = UamTextDark,
    tertiary = UamTurquoiseSecondary,
    background = UamTextDark,
    surface = ColorTokens.darkSurface,
    onBackground = UamWhite,
    onSurface = UamWhite
)

private val LightColorScheme = lightColorScheme(
    primary = UamTurquoise,
    onPrimary = UamWhite,
    primaryContainer = UamLightBackground,
    onPrimaryContainer = UamTextDark,
    secondary = UamInnovationYellow,
    onSecondary = UamTextDark,
    secondaryContainer = ColorTokens.yellowSoft,
    onSecondaryContainer = UamTextDark,
    tertiary = UamTurquoiseAccent,
    onTertiary = UamWhite,
    background = UamLightBackground,
    onBackground = UamTextDark,
    surface = UamWhite,
    onSurface = UamTextDark,
    outline = UamGray
)

@Composable
fun Sistema_Innovacion_FrontendTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

private object ColorTokens {
    val darkSurface = UamTextDark.copy(alpha = 0.92f)
    val yellowSoft = UamInnovationYellow.copy(alpha = 0.22f)
}
