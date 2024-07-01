package com.nottherobot.todoapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomColors(
    val supportSeparator: Color,
    val supportOverlay: Color,
    val labelPrimary: Color,
    val labelSecondary: Color,
    val labelTertiary: Color,
    val labelDisable: Color,
    val red: Color,
    val green: Color,
    val blue: Color,
    val lightBlue: Color,
    val gray: Color,
    val grayLight: Color,
    val white: Color,
    val backPrimary: Color,
    val backSecondary: Color,
    val backElevated: Color,
    val shadowGradient: List<Color>
)
