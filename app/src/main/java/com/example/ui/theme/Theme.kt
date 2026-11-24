package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val MysticDarkColorScheme = darkColorScheme(
    primary = CyanElectric,
    onPrimary = NavyAbyss,
    primaryContainer = CyanContainer,
    onPrimaryContainer = PureWhite,
    secondary = SkyAccent,
    onSecondary = NavyAbyss,
    secondaryContainer = SkyContainer,
    onSecondaryContainer = CrispWhite,
    tertiary = EmeraldSuccess,
    onTertiary = NavyAbyss,
    tertiaryContainer = EmeraldContainer,
    onTertiaryContainer = CrispWhite,
    background = NavyAbyss,
    onBackground = CrispWhite,
    surface = NavySurface,
    onSurface = CrispWhite,
    surfaceVariant = NavyCard,
    onSurfaceVariant = SlateLight,
    surfaceContainerHigh = NavyElevated,
    outline = NavyBorder,
    outlineVariant = NavyBorder,
    error = CoralError,
    onError = NavyAbyss,
    errorContainer = CoralContainer,
    onErrorContainer = CrispWhite
)

private val MysticLightColorScheme = lightColorScheme(
    primary = LightCyanAccent,
    onPrimary = PureWhite,
    primaryContainer = ColorTokens.LightCyanContainer,
    onPrimaryContainer = LightNavyPrimary,
    secondary = LightNavyPrimary,
    onSecondary = PureWhite,
    secondaryContainer = LightSurfaceVariant,
    onSecondaryContainer = LightNavyPrimary,
    tertiary = ColorTokens.LightEmerald,
    onTertiary = PureWhite,
    background = LightIceBackground,
    onBackground = LightNavyPrimary,
    surface = LightSurface,
    onSurface = LightNavyPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = ColorTokens.LightSlateText,
    outline = ColorTokens.LightOutline,
    error = ColorTokens.LightError,
    onError = PureWhite
)

private object ColorTokens {
    val LightCyanContainer = androidx.compose.ui.graphics.Color(0xFFCFFAFE)
    val LightEmerald = androidx.compose.ui.graphics.Color(0xFF059669)
    val LightSlateText = androidx.compose.ui.graphics.Color(0xFF334155)
    val LightOutline = androidx.compose.ui.graphics.Color(0xFFCBD5E1)
    val LightError = androidx.compose.ui.graphics.Color(0xFFDC2626)
}

val MysticShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun MysticAITheme(
    darkTheme: Boolean = true,
    isBengali: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) MysticDarkColorScheme else MysticLightColorScheme
    val typography = buildMysticTypography(isBengali = isBengali)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = MysticShapes,
        content = content
    )
}
