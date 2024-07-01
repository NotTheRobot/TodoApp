package com.nottherobot.todoapp.ui.screens.edittodo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nottherobot.todoapp.NavigationDestination
import com.nottherobot.todoapp.models.ui.Importance
import com.nottherobot.todoapp.ui.theme.AppTheme
import java.time.LocalDate

object EditTodoScreen : NavigationDestination {
    override val route: String = "EditTodo"
    private const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun EditTodoScreen(
    onNavigateUp: () -> Unit,
    vm: EditTodoViewModel = viewModel(factory = EditTodoViewModel.Factory)
) {
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
) {
    Scaffold(
        topBar = {
            EditTodoTopBar(
                onCancelClick = { onNavigateUp() },
                onSaveClick = {
                    onSaveClick()
                    onNavigateUp()
                },
                isSaveButtonEnabled = textState.value.isNotBlank()
            )
        },
        containerColor = AppTheme.colors.backPrimary
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(8.dp))
            TaskDescription(
                textState
            )
            Spacer(modifier = Modifier.height(12.dp))
            ImportanceDropdown(
                importanceState
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            DeadlineView(
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
}

@Preview
@Composable
fun EditTodoScreenPreview() {
    AppTheme {
        EditTodoScreen(
            onNavigateUp = {},
            onSaveClick = {},
            textState = remember {
                mutableStateOf("Здесь текст вашей таски")
            },
            importanceState = remember {
                mutableStateOf(Importance.High)
            },
            deadlineState = remember {
                mutableStateOf(LocalDate.now())
            },
            isAbleToDelete = false,
            onDeleteClick = {}
        )
    }
}
