package com.nottherobot.todoapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nottherobot.todoapp.NavigationDestination
import com.nottherobot.todoapp.R
import com.nottherobot.todoapp.models.ui.Importance
import com.nottherobot.todoapp.ui.theme.AppTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object EditTodoScreen: NavigationDestination {
    override val route: String = "EditTodo"
    private const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun EditTodoScreen(
    onNavigateUp: () -> Unit,
    vm: EditTodoViewModel = viewModel(factory = EditTodoViewModel.Factory)
){
    EditTodoScreen(
        onNavigateUp = { onNavigateUp() },
        onSaveClick = { vm.onSaveClick() },
        textState = vm.text,
        importanceState = vm.importance,
        deadlineState = vm.deadlineDate,
        isAbleToDelete = vm.item.value != null,
        onDeleteClick = { vm.onDeleteClick() }
    )
}

@Composable
fun EditTodoScreen(
    onNavigateUp: () -> Unit,
    onSaveClick: () -> Unit,
    textState: MutableState<String>,
    importanceState: MutableState<Importance>,
    deadlineState: MutableState<LocalDate?>,
    isAbleToDelete: Boolean,
    onDeleteClick: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backPrimary)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(32.dp))
        EditTodoTopBar(
            onCancelClick = { onNavigateUp() },
            onSaveClick = {
                onSaveClick()
                onNavigateUp()
            },
            isSaveButtonEnabled = textState.value != ""
        )
        Spacer(modifier = Modifier.height(8.dp))
        TaskDescription(
            textState
        )
        Spacer(modifier = Modifier.height(12.dp))
        ImportanceDropdown(
            importanceState
        )
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        DeadLineView(
            deadlineState
        )
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))
        DeleteView(
            isAbleToDelete,
            onDeleteClick = {
                onDeleteClick()
                onNavigateUp()
            }
        )
        Spacer(modifier = Modifier.height(48.dp))
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDescription(
    textState: MutableState<String>
){
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

@Composable
fun ImportanceDropdown(
    importanceState: MutableState<Importance>
) {
    var importance by importanceState
    var expanded by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .clickable(role = Role.DropdownList) {
                expanded = !expanded
            }
            .heightIn(min = 72.dp)
            .fillMaxWidth()
    ){
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(16.dp, (-72).dp),
            modifier = Modifier
                .widthIn(min = 164.dp)
                .shadow(8.dp, spotColor = Color.Black, ambientColor = Color.Black)
                .padding(1.dp)
                .background(AppTheme.colors.backSecondary)
        ) {
            Text(
                text = stringResource(id = R.string.none),
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .padding(16.dp)
                    .clickable(role = Role.Button) {
                        importance = Importance.Default
                        expanded = false
                    },
                color = AppTheme.colors.labelPrimary,
                style = AppTheme.type.body,
            )
            Text(
                text = stringResource(id = R.string.low),
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .padding(16.dp)
                    .clickable(role = Role.Button) {
                        importance = Importance.Low
                        expanded = false
                    },
                color = AppTheme.colors.labelPrimary,
                style = AppTheme.type.body
            )
            Text(
                text = stringResource(id = R.string.high),
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .padding(16.dp)
                    .clickable(role = Role.Button) {
                        importance = Importance.High
                        expanded = false
                    },
                color = AppTheme.colors.red,
                style = AppTheme.type.body
            )
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
                text = stringResource(id = importance.getStringId()),
                color = if(importance == Importance.High) AppTheme.colors.red else AppTheme.colors.labelTertiary,
                style = AppTheme.type.subhead,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadLineView(
    deadlineState: MutableState<LocalDate?>
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
                            .toLocalDate()
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

@Composable
fun DeleteView(
    isEnabled: Boolean,
    onDeleteClick: () -> Unit
) {
    val contentColor = if (isEnabled) AppTheme.colors.red else AppTheme.colors.labelDisable
    val modifier = if(isEnabled){
        Modifier.clickable(role = Role.Button) { onDeleteClick() }
    }else{
        Modifier
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(72.dp)
            .fillMaxWidth()
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
fun EditTodoTopBarPreview(){
    AppTheme {
        EditTodoTopBar(
            onCancelClick = { },
            onSaveClick = { },
            isSaveButtonEnabled = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskDescriptionPreview(){
    AppTheme{
        val text = remember {
            mutableStateOf("clean ass")
        }
        TaskDescription(textState = text)
    }
}

@Preview(showBackground = true)
@Composable
fun ImportanceDropDownPreview(){
    AppTheme{
        val importance = remember {
            mutableStateOf(Importance.Default)
        }
        ImportanceDropdown(importanceState = importance)
    }
}

@Preview(showBackground = true)
@Composable
fun DeadlineView(){
    AppTheme{
        val deadline = remember {
            mutableStateOf(LocalDate.now())
        }
        DeadLineView(deadlineState = deadline)
    }
}

@Preview(showBackground = true)
@Composable
fun DeleteViewPreview(){
    AppTheme{
        DeleteView(isEnabled = true) {}
    }
}
