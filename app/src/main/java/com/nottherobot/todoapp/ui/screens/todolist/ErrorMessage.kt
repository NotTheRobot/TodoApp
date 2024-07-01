package com.nottherobot.todoapp.ui.screens.todolist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.layoutId
import com.nottherobot.todoapp.ui.theme.AppTheme

@Composable
fun ErrorMessage(
    text: String
) {
    if (text.trim() != "") {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(AppTheme.colors.backPrimary)
                .padding(4.dp)
                .layoutId("errorMessage")
        ) {
            Text(
                text = text,
                color = AppTheme.colors.labelSecondary,
                style = AppTheme.type.body
            )
        }
    }
}

@Composable
@Preview
fun ErrorMessagePreview() {
    AppTheme {
        ErrorMessage(text = "Error")
    }
}
