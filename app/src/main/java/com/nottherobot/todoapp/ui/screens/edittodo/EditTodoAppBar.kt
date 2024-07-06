package com.nottherobot.todoapp.ui.screens.edittodo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nottherobot.todoapp.R
import com.nottherobot.todoapp.ui.theme.AppTheme

@Composable
fun EditTodoTopBar(
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
    isSaveButtonEnabled: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(56.dp)
    ) {
        IconButton(
            onClick = {
                onCancelClick()
            },
            modifier = Modifier
                .padding(start = 4.dp)
                .size(48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.close),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(AppTheme.colors.labelPrimary)
            )
        }

        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier.fillMaxSize()
        ) {
            TextButton(
                onClick = {
                    onSaveClick()
                },
                modifier = Modifier
                    .padding(end = 4.dp)
                    .height(48.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = AppTheme.colors.blue,
                    disabledContentColor = AppTheme.colors.labelDisable
                ),
                enabled = isSaveButtonEnabled
            ) {
                Text(
                    text = stringResource(id = R.string.save),
                    style = AppTheme.type.button,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditTodoTopBarPreview() {
    AppTheme {
        EditTodoTopBar(
            onCancelClick = { },
            onSaveClick = { },
            isSaveButtonEnabled = true
        )
    }
}
