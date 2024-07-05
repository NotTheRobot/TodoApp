package com.nottherobot.todoapp.ui.screens.todolist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nottherobot.todoapp.NavigationDestination
import com.nottherobot.todoapp.R
import com.nottherobot.todoapp.repository.generateItems
import com.nottherobot.todoapp.ui.models.TodoItem
import com.nottherobot.todoapp.ui.theme.AppTheme

object TodoListScreen : NavigationDestination {
    override val route: String = "TodoList"
}

@Composable
fun TodoListScreen(
    vm: TodoListViewModel = viewModel(factory = TodoListViewModel.Factory),
    navigateToEditTodo: (String?) -> Unit
) {
    val list by vm.todoList.collectAsState()
    TodoListScreen(
        itemsList = list,
        navigateToEdit = { navigateToEditTodo(it) },
        onCheckboxClicked = { todoItem, b -> vm.onCheckboxClicked(todoItem, b) },
        isShowDoneState = vm.isShowDone,
        doneTasksCount = vm.doneTasksCount.intValue,
        vm.errorText.value
    )
}

@Composable
fun TodoListScreen(
    itemsList: List<TodoItem>,
    navigateToEdit: (String?) -> Unit,
    onCheckboxClicked: (TodoItem, Boolean) -> Unit,
    isShowDoneState: MutableState<Boolean>,
    doneTasksCount: Int,
    errorText: String
) {
    var currentOffset by remember { mutableFloatStateOf(76.0f) }
    val maxPx = remember { 76.0f }
    val minPx = remember { 0.0f }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val off = currentOffset
                if (currentOffset + available.y > maxPx) {
                    currentOffset = maxPx
                    return Offset(0f, maxPx - off)
                }
                if (currentOffset + available.y < minPx) {
                    currentOffset = minPx
                    return Offset(0f, minPx - off)
                }

                currentOffset += available.y
                return Offset(0f, available.y)
            }
        }
    }

    val context = LocalContext.current
    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.motion_screne)
            .readBytes()
            .decodeToString()
    }

    MotionLayout(
        motionScene = MotionScene(motionScene),
        progress = 1f - currentOffset / maxPx,
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backPrimary),
    ) {
        TodoLazyList(
            itemsList,
            { todoItem, b -> onCheckboxClicked(todoItem, b) },
            { navigateToEdit(it) },
            isShowDoneState.value,
            nestedScrollConnection
        )
        TodoListHeader(
            doneTasksCount,
            isShowDoneState,
            this@MotionLayout
        )
        FloatingActionButton(
            onClick = { navigateToEdit(null) },
            modifier = Modifier
                .size(56.dp)
                .layoutId("fab"),
            shape = CircleShape,
            containerColor = AppTheme.colors.blue,
        ) {
            Image(
                painter = painterResource(id = R.drawable.add),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(AppTheme.colors.white)
            )
        }
        ErrorMessage(text = errorText)
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListScreenPreview() {
    AppTheme {
        val items = generateItems(30)
        val isShowDoneState = remember { mutableStateOf(true) }
        TodoListScreen(
            itemsList = items,
            navigateToEdit = { },
            onCheckboxClicked = { todoItem, b -> },
            isShowDoneState = isShowDoneState,
            doneTasksCount = 5,
            "error"
        )
    }
}
