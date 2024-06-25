package com.nottherobot.todoapp.ui.screens.edittodo

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nottherobot.todoapp.R
import com.nottherobot.todoapp.ui.theme.AppTheme

@Composable
fun DeleteView(
    isEnabled: Boolean,
    onDeleteClick: () -> Unit,
) {
    val contentColor = if (isEnabled) AppTheme.colors.red else AppTheme.colors.labelDisable

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(72.dp)
            .fillMaxWidth()
            .clickable(
                role = Role.Button,
                enabled = isEnabled
            ) {
                onDeleteClick()
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.delete),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(contentColor)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(id = R.string.delete),
            color = contentColor,
            style = AppTheme.type.body
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeleteViewPreview() {
    AppTheme {
        DeleteView(isEnabled = true) {}
    }
}
