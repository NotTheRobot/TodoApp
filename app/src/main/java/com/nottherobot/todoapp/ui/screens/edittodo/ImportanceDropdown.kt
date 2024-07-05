package com.nottherobot.todoapp.ui.screens.edittodo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.nottherobot.todoapp.R
import com.nottherobot.todoapp.ui.models.Importance
import com.nottherobot.todoapp.ui.theme.AppTheme

@Composable
fun ImportanceDropdown(
    importanceState: MutableState<Importance>
) {
    var currentImportance by importanceState
    var expanded by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .clickable(role = Role.DropdownList) {
                expanded = !expanded
            }
            .heightIn(min = 72.dp)
            .fillMaxWidth()
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .widthIn(min = 164.dp)
                .shadow(8.dp, spotColor = Color.Black, ambientColor = Color.Black)
                .padding(1.dp)
                .background(AppTheme.colors.backSecondary),
            offset = DpOffset(16.dp, (-72).dp),
        ) {
            for (importance in Importance.entries) {
                Text(
                    text = stringResource(id = importance.stringRes),
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .padding(16.dp)
                        .clickable(role = Role.Button) {
                            currentImportance = importance
                            expanded = false
                        },
                    color = colorResource(id = importance.colorRes),
                    style = AppTheme.type.body,
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = R.string.importance),
                color = AppTheme.colors.labelPrimary,
                style = AppTheme.type.body,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = currentImportance.stringRes),
                color = if (currentImportance == Importance.High) AppTheme.colors.red else AppTheme.colors.labelTertiary,
                style = AppTheme.type.subhead,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImportanceDropDownPreview() {
    AppTheme {
        val importance = remember {
            mutableStateOf(Importance.Default)
        }
        ImportanceDropdown(importanceState = importance)
    }
}
