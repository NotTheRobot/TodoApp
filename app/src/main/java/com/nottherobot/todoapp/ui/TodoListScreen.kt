package com.nottherobot.todoapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionLayoutScope
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nottherobot.todoapp.NavigationDestination
import com.nottherobot.todoapp.R
import com.nottherobot.todoapp.models.ui.Importance
import com.nottherobot.todoapp.models.ui.TodoItem
import com.nottherobot.todoapp.repository.TodoItemsRepository
import com.nottherobot.todoapp.repository.generateItems
import com.nottherobot.todoapp.ui.theme.AppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object TodoListScreen: NavigationDestination{
    override val route: String = "TodoList"
}

@Composable
fun TodoListScreen(
    vm: TodoListViewModel = viewModel(factory = TodoListViewModel.Factory),
    navigateToEditTodo: (String?) -> Unit
){
    val list by vm.todoList.collectAsState()
    TodoListScreen(
        itemsList = list,
        navigateToEdit = { navigateToEditTodo(it) },
        onCheckboxClicked = {todoItem, b ->  vm.onCheckboxClicked(todoItem, b) },
        isShowDoneState = vm.isShowDone,
        doneTasksCount = vm.doneTasksCount.intValue
    )
}

@Composable
fun TodoListScreen(
    itemsList: List<TodoItem>,
    navigateToEdit: (String?) -> Unit,
    onCheckboxClicked: (TodoItem, Boolean) -> Unit,
    isShowDoneState: MutableState<Boolean>,
    doneTasksCount: Int
){
    var currentOffset by remember{ mutableFloatStateOf(76.0f) }
    val maxPx = remember { 76.0f }
    val minPx = remember { 0.0f }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val off = currentOffset
                if(currentOffset + available.y > maxPx){
                    currentOffset = maxPx
                    return Offset(0f, maxPx - off)
                }
                if(currentOffset + available.y < minPx){
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
            { todoItem, b -> onCheckboxClicked(todoItem, b)},
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
    }
}

@Composable
fun TodoListHeader(
    doneTasksCount: Int,
    isShowDoneState: MutableState<Boolean>,
    motionLayoutScope: MotionLayoutScope
    ) {
    var isChecked by isShowDoneState

    Text(
        text = stringResource(id = R.string.title_todo_list),
        modifier = Modifier.layoutId("title"),
        color = AppTheme.colors.labelPrimary,
        fontSize = motionLayoutScope.customFontSize("title", "fontSize"),
        fontWeight = FontWeight.W500,
        lineHeight = motionLayoutScope.customFontSize("title", "lineHeight"),
        letterSpacing = motionLayoutScope.customFontSize("title", "letterSpacing"),

    )
    Text(
        text = stringResource(id = R.string.done) + " - $doneTasksCount",
        color = AppTheme.colors.labelTertiary,
        style = AppTheme.type.body,
        modifier = Modifier.layoutId("subtitle")
    )
    IconToggleButton(
        checked = isChecked,
        onCheckedChange = {
            isChecked = it
        },
        modifier = Modifier
            .size(48.dp)
            .layoutId("eye")
    ) {
        Image(
            painter = painterResource(id = if (isChecked) R.drawable.eye_closed else R.drawable.eye_open),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(AppTheme.colors.blue)
        )
    }
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .layoutId("headerBox")
            .fillMaxWidth()
    ){
        Column {
            Box(modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
                .background(AppTheme.colors.backPrimary)
            )
            Box(modifier = Modifier
                .height(4.dp)
                .fillMaxWidth()
                .background(brush = Brush.verticalGradient(AppTheme.colors.shadowGradient))
            )
        }

    }

}

@Composable
fun TodoLazyList(
    list: List<TodoItem>,
    onCheckboxClick: (TodoItem, Boolean) -> Unit,
    onItemClick: (String?) -> Unit,
    isShowDoneState: Boolean,
    nestedScrollConnection: NestedScrollConnection
) {

    BoxWithConstraints(
        modifier = Modifier
            .layoutId("lazyColumn")
            .padding(start = 8.dp, end = 8.dp, bottom = 88.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AppTheme.colors.backSecondary)
    ) {
        val textWidth = remember { maxWidth - 96.dp }
        LazyColumn(
            modifier = Modifier.nestedScroll(nestedScrollConnection)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(list) { item ->
                if (isShowDoneState || !item.isDone) {
                    TodoListItem(item, textWidth, onCheckboxClick, onItemClick)
                }
            }
            item {
                Box(modifier = Modifier
                    .heightIn(min = 48.dp)
                    .fillMaxWidth()
                    .clickable(role = Role.Button) {
                        onItemClick(null)
                    }
                    .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.new_task),
                        modifier = Modifier.padding(start = 48.dp),
                        color = AppTheme.colors.labelTertiary,
                        style = AppTheme.type.body
                    )
                }
            }
        }
    }
}

@Composable
fun TodoListItem(
    item: TodoItem,
    textWidth: Dp,
    onCheckboxClick: (TodoItem, Boolean) -> Unit,
    onItemClick: (String) -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button) {
                onItemClick(item.id)
            }
    ) {
        IconToggleButton(
            checked = item.isDone,
            onCheckedChange = { isDone -> onCheckboxClick(item, isDone) },
        ) {
            val checkboxDrawableColor = if (item.isDone) {
                AppTheme.colors.green
            } else if(item.importance == Importance.High){
                AppTheme.colors.red
            }else{
                AppTheme.colors.supportSeparator
            }
            val checkboxDrawableId = if (item.isDone) {
                R.drawable.checked_checkbox
            } else if(item.importance == Importance.High){
                R.drawable.unchecked_checkbox_high_prior
            }else{
                R.drawable.unchecked_checkbox_default
            }
            Image(
                painter = painterResource(id = checkboxDrawableId),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(checkboxDrawableColor)
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .width(textWidth)
                .padding(top = 12.dp, bottom = 12.dp)
        ) {
            Row {
                if(item.importance != Importance.Default){
                    Image(painter = painterResource(
                        id = if(item.importance == Importance.High) R.drawable.high_priority else R.drawable.low_priority),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(if(item.importance == Importance.High) AppTheme.colors.red else AppTheme.colors.gray)
                    )
                }
                Text(
                    text = item.text,
                    color = if (item.isDone) AppTheme.colors.labelTertiary else AppTheme.colors.labelPrimary,
                    style = AppTheme.type.body,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (item.isDone) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            if (item.deadlineDate != null) {
                val formatter = remember {
                    DateTimeFormatter.ofPattern("d MMMM yyyy")
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.deadlineDate.format(formatter),
                    color = AppTheme.colors.labelTertiary,
                    style = AppTheme.type.subhead
                )
            }
        }

        Box(
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.info_outline),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(AppTheme.colors.labelTertiary)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListHeaderPreviewEnd(){
    AppTheme {
        val done = remember { 0 }
        val isShowDone = remember {
            mutableStateOf(true)
        }
        val context = LocalContext.current
        val motionScene = remember {
            context.resources
                .openRawResource(R.raw.motion_screne)
                .readBytes()
                .decodeToString()
        }
        MotionLayout(motionScene = MotionScene(motionScene), progress = 1f) {
            TodoListHeader(
                doneTasksCount = done,
                isShowDoneState = isShowDone,
                motionLayoutScope = this@MotionLayout
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListHeaderPreviewStart(){
    AppTheme {
        val done = remember { 0 }
        val isShowDone = remember {
            mutableStateOf(true)
        }
        val context = LocalContext.current
        val motionScene = remember {
            context.resources
                .openRawResource(R.raw.motion_screne)
                .readBytes()
                .decodeToString()
        }
        MotionLayout(motionScene = MotionScene(motionScene), progress = 0f) {
            TodoListHeader(
                doneTasksCount = done,
                isShowDoneState = isShowDone,
                motionLayoutScope = this@MotionLayout
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoItemPreview() {
    AppTheme {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            TodoListItem(
                item = TodoItem(
                    id = "0",
                    text = "Clean ass",
                    importance = Importance.High,
                    deadlineDate = LocalDate.now(),
                    isDone = false,
                    creationDate = LocalDate.now(),
                    modificationDate = LocalDate.now(),
                ),
                textWidth = maxWidth - 96.dp,
                onCheckboxClick = { todoItem, b -> },
                onItemClick = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoLazyListPreview(){
    AppTheme {
        val items = generateItems(30)
        val nsc = remember {
            object : NestedScrollConnection {

            }
        }
        TodoLazyList(
            list = items,
            onCheckboxClick = { todoItem, b -> },
            onItemClick = { },
            isShowDoneState = false,
            nestedScrollConnection = nsc
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListScreenPreview(){
    AppTheme {
        val items = generateItems(30)
        val isShowDoneState = remember { mutableStateOf(true) }
        TodoListScreen(
            itemsList = items,
            navigateToEdit = { },
            onCheckboxClicked = { todoItem, b -> },
            isShowDoneState = isShowDoneState,
            doneTasksCount = 5
        )
    }
}
