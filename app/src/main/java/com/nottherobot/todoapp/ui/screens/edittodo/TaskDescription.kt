package com.nottherobot.todoapp.ui.screens.edittodo

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nottherobot.todoapp.R
import com.nottherobot.todoapp.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDescription(
    textState: MutableState<String>
) {
    var text by textState
    BasicTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .heightIn(min = 112.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .shadow(8.dp)
            .padding(bottom = 2.dp, start = 1.dp, end = 1.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AppTheme.colors.backSecondary)
            .padding(16.dp),
        textStyle = AppTheme.type.body.copy(color = AppTheme.colors.labelPrimary),
        cursorBrush = SolidColor(AppTheme.colors.labelPrimary),
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = text,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = false,
                interactionSource = remember { MutableInteractionSource() },
                visualTransformation = VisualTransformation.None,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.what_to_do),
                        color = AppTheme.colors.labelTertiary,
                        style = AppTheme.type.body,
                    )
                },
                contentPadding = PaddingValues(0.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun TaskDescriptionPreview() {
    AppTheme {
        val text = remember {
            mutableStateOf("clean ass")
        }
        TaskDescription(textState = text)
    }
}
