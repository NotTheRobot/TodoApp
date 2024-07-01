package com.nottherobot.todoapp

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.nottherobot.todoapp.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                setSystemBarsColor()
                TodoAppScreen()
            }
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun setSystemBarsColor(){
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        window.statusBarColor = AppTheme.colors.backPrimary.toArgb()
        window.navigationBarColor = AppTheme.colors.backPrimary.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isSystemInDarkTheme()
    }
}
