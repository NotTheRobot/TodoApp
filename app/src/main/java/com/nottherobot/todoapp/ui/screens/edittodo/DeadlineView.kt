package com.nottherobot.todoapp.ui.screens.edittodo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nottherobot.todoapp.R
import com.nottherobot.todoapp.ui.theme.AppTheme
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadlineView(
    deadlineState: MutableState<LocalDateTime?>
) {
    var deadline by deadlineState
    var isDialogOpen by remember {
        mutableStateOf(false)
    }
    val formatter = remember {
        DateTimeFormatter.ofPattern("dd MMMM yyyy")
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(role = Role.DropdownList) {
                isDialogOpen = true
            }
            .height(72.dp)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.do_until),
                modifier = Modifier,
                color = AppTheme.colors.labelPrimary,
                style = AppTheme.type.body
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (deadline != null) {
                Text(
                    text = deadline!!.format(formatter),
                    modifier = Modifier,
                    color = AppTheme.colors.blue,
                    style = AppTheme.type.subhead
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Switch(
                checked = deadline != null,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        isDialogOpen = true
                    } else {
                        deadline = null
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = AppTheme.colors.blue,
                    uncheckedThumbColor = AppTheme.colors.backElevated,
                    checkedTrackColor = AppTheme.colors.lightBlue,
                    uncheckedTrackColor = AppTheme.colors.supportOverlay,
                    checkedBorderColor = Color.Transparent,
                    uncheckedBorderColor = Color.Transparent,
                )
            )
        }

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = LocalDate.now().toEpochDay() * 24 * 3600 * 1000,
        )

        if (isDialogOpen) {
            DatePickerDialog(
                onDismissRequest = {
                    isDialogOpen = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            deadline = Instant
                                .ofEpochMilli(datePickerState.selectedDateMillis!!)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                            isDialogOpen = false
                        },
                        enabled = datePickerState.selectedDateMillis != null
                    ) {
                        Text(
                            text = stringResource(id = R.string.ok),
                            modifier = Modifier,
                            color = AppTheme.colors.blue,
                            style = AppTheme.type.button
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        isDialogOpen = false
                    }) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            modifier = Modifier,
                            color = AppTheme.colors.blue,
                            style = AppTheme.type.button
                        )
                    }
                },
                colors = DatePickerDefaults.colors(
                    containerColor = AppTheme.colors.backSecondary,
                )
            ) {
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier,
                    colors = DatePickerDefaults.colors(
                        dividerColor = AppTheme.colors.blue,
                        todayContentColor = AppTheme.colors.blue,
                        todayDateBorderColor = Color.Transparent,
                        dayContentColor = AppTheme.colors.labelPrimary,
                        selectedDayContainerColor = AppTheme.colors.blue,
                        selectedDayContentColor = AppTheme.colors.white,
                        weekdayContentColor = AppTheme.colors.labelSecondary,
                        titleContentColor = AppTheme.colors.labelPrimary,
                        headlineContentColor = AppTheme.colors.labelPrimary,
                        selectedYearContainerColor = AppTheme.colors.blue,
                        selectedYearContentColor = AppTheme.colors.white,
                        currentYearContentColor = AppTheme.colors.blue,
                        yearContentColor = AppTheme.colors.labelPrimary,
                        subheadContentColor = AppTheme.colors.blue,
                        navigationContentColor = AppTheme.colors.labelPrimary,
                        containerColor = AppTheme.colors.backSecondary,
                    ),
                    showModeToggle = false
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeadlineViewPreview() {
    AppTheme {
        val deadline = remember {
            mutableStateOf(LocalDateTime.now())
        }
        DeadlineView(deadlineState = deadline)
    }
}
