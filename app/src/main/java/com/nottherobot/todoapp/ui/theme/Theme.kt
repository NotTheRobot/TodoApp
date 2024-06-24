package com.nottherobot.todoapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.nottherobot.todoapp.R

val LocalCustomType = staticCompositionLocalOf {
    CustomTypography(
        TextStyle.Default,
        TextStyle.Default,
        TextStyle.Default,
        TextStyle.Default,
        TextStyle.Default,
    )
}

val LocalCustomColors = staticCompositionLocalOf {
    CustomColors(
        Color.Unspecified,
        Color.Unspecified,
        Color.Unspecified,
        Color.Unspecified,
        Color.Unspecified,
        Color.Unspecified,
        Color.Unspecified,
        Color.Unspecified,
        Color.Unspecified,
        Color.Unspecified,
        Color.Unspecified,
        Color.Unspecified,
        Color.Unspecified,
        Color.Unspecified,
        Color.Unspecified,
        Color.Unspecified,
        listOf()
    )
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val customColors = CustomColors(
        supportSeparator = colorResource(id = R.color.support_separator),
        supportOverlay = colorResource(id = R.color.support_overlay),
        labelPrimary = colorResource(id = R.color.label_primary),
        labelSecondary = colorResource(id = R.color.label_secondary),
        labelTertiary = colorResource(id = R.color.label_tertiary),
        labelDisable = colorResource(id = R.color.label_disable),
        red = colorResource(id = R.color.red),
        green = colorResource(id = R.color.green),
        blue = colorResource(id = R.color.blue),
        lightBlue = colorResource(id = R.color.light_blue),
        gray = colorResource(id = R.color.gray),
        grayLight = colorResource(id = R.color.gray_light),
        white = colorResource(id = R.color.white),
        backPrimary = colorResource(id = R.color.back_primary),
        backSecondary = colorResource(id = R.color.back_secondary),
        backElevated = colorResource(id = R.color.back_elevated),
        shadowGradient = listOf(
            Color(0, 0, 0, 0x4D),
            Color.Transparent
        )
    )

    val customTypography = CustomTypography(
        largeTitle = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W500,
            fontSize = 32.sp,
            lineHeight = 38.sp,
            letterSpacing = 0.sp
        ),
        title = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W500,
            fontSize = 20.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.5.sp
        ),
        button = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.16.sp
        ),
        body = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp
        ),
        subhead = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp
        ),
    )

    CompositionLocalProvider(
        LocalCustomColors provides customColors,
        LocalCustomType provides customTypography,
        content = content
    )
}

object AppTheme{
    val colors : CustomColors
        @Composable
        get() = LocalCustomColors.current

    val type: CustomTypography
        @Composable
        get() = LocalCustomType.current
}
