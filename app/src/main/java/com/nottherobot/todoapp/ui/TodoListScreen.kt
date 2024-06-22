package com.nottherobot.todoapp.ui

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.composableLambdaNInstance
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nottherobot.todoapp.NavigationDestination
import com.nottherobot.todoapp.R
import com.nottherobot.todoapp.models.ui.Importance
import com.nottherobot.todoapp.models.ui.TodoItem
import java.time.format.DateTimeFormatter

object TodoListScreen: NavigationDestination{
    override val route: String = "TodoList"
}

@Composable
fun TodoListScreen(
    navigateToEditTodo: (String?) -> Unit
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

    MotionLayout(
        motionScene = mainScreenMotionScene(),
        progress = 1f - currentOffset / maxPx,
        modifier = Modifier
            .background(colorResource(id = R.color.back_primary))
            .fillMaxSize(),
    ) {
        val vm: TodoListViewModel = viewModel(factory = TodoListViewModel.Factory)
        val list by vm.todoList.collectAsState()

        TodoLazyList(
            list,
            { todoItem, b -> vm.onCheckboxClicked(todoItem, b)},
            { navigateToEditTodo(it) },
            vm.isShowDone,
            nestedScrollConnection
        )
        TodoListHeader(
            vm.doneTasksCount,
            vm.isShowDone,
        )
        FloatingActionButton(
            onClick = { navigateToEditTodo(null) },
            modifier = Modifier
                .size(56.dp)
                .layoutId("fab"),
            shape = CircleShape,
            containerColor = colorResource(id = R.color.blue),
        ) {
            Image(
                painter = painterResource(id = R.drawable.add),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(colorResource(id = R.color.white))
            )
        }
    }
}

@Composable
fun TodoListHeader(
    doneTasksCount: MutableIntState,
    isShowDoneState: MutableState<Boolean>,
    ){
    var isChecked by isShowDoneState

    BoxWithConstraints(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.layoutId("header")
    ) {
        val animPosition = maxHeight - 88.dp
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(1f - animPosition / 76.dp)
                .shadow(8.dp - animPosition / 19, RectangleShape)
                .padding(bottom = 4.dp)
                .background(colorResource(id = R.color.back_primary))
        )
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (title, subtitle, icon) = createRefs()
            Text(
                text = stringResource(id = R.string.title_todo_list),
                color = colorResource(id = R.color.label_primary),
                style = if (animPosition > 10.dp) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top, 40.dp + animPosition * 0.55f)
                    start.linkTo(parent.start, 16.dp + animPosition * 0.55f)
                }
            )
            Text(
                text = stringResource(id = R.string.done) + " - ${doneTasksCount.intValue}",
                color = colorResource(id = R.color.label_tertiary),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .alpha(animPosition / 76.dp)
                    .constrainAs(subtitle) {
                        top.linkTo(title.bottom, 8.dp)
                        start.linkTo(title.start)
                    }
            )
            IconToggleButton(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                },
                modifier = Modifier
                    .size(48.dp)
                    .padding(4.dp)
                    .constrainAs(icon) {
                        top.linkTo(title.top)
                        bottom.linkTo(title.bottom)
                        end.linkTo(parent.end)
                    }
            ) {
                Image(
                    painter = painterResource(id = if (isChecked) R.drawable.eye_closed else R.drawable.eye_open),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(colorResource(id = R.color.blue))
                
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoLazyList(
    list: List<TodoItem>,
    onCheckboxClick: (TodoItem, Boolean) -> Unit,
    onItemClick: (String?) -> Unit,
    isShowDoneState: MutableState<Boolean>,
    nestedScrollConnection: NestedScrollConnection
){
    val isShowDone by isShowDoneState

    BoxWithConstraints(
        modifier = Modifier
            .layoutId("lazyColumn")
            .nestedScroll(nestedScrollConnection)
    ) {
        val textWidth = remember { maxWidth - 96.dp }
        Box(modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(id = R.color.back_secondary))
        ) {
            LazyColumn{
                stickyHeader {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(list) { item ->
                    if(isShowDone || !item.isDone){
                        TodoItem(item, textWidth, onCheckboxClick, onItemClick)
                    }
                }
                stickyHeader {
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
                            color = colorResource(id = R.color.label_tertiary),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TodoItem(
    item: TodoItem,
    textWidth: Dp,
    onCheckboxClick: (TodoItem, Boolean) -> Unit,
    onItemClick: (String) -> Unit
){
    val checkboxDrawableId = if (item.isDone) {
        R.drawable.checked_checkbox
    } else if(item.importance == Importance.High){
        R.drawable.unchecked_checkbox_high_prior
    }else{
        R.drawable.unchecked_checkbox_default
    }

    val checkboxDrawableColor = if (item.isDone) {
        R.color.green
    } else if(item.importance == Importance.High){
        R.color.red
    }else{
        R.color.support_separator
    }
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
            Image(
                painter = painterResource(id = checkboxDrawableId),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(colorResource(checkboxDrawableColor))
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
                        colorFilter = ColorFilter.tint(colorResource(id = if(item.importance == Importance.High) R.color.red else R.color.gray))
                    )
                }
                Text(
                    text = item.text,
                    color = if (item.isDone) colorResource(id = R.color.label_tertiary) else colorResource(
                        id = R.color.label_primary
                    ),
                    style = MaterialTheme.typography.bodyMedium,
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
                    color = colorResource(id = R.color.label_tertiary),
                    style = MaterialTheme.typography.bodySmall
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
                colorFilter = ColorFilter.tint(colorResource(id = R.color.label_tertiary))
            )
        }
    }
}

@Composable
fun mainScreenMotionScene(): MotionScene {
    return MotionScene { // this: MotionSceneScope
        val header = createRefFor("header")
        val lazyColumn = createRefFor("lazyColumn")
        val fab = createRefFor("fab")
        defaultTransition(
            from = constraintSet { // this: ConstraintSetScope
                constrain(header) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.top, (-164).dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                constrain(lazyColumn) {
                    top.linkTo(header.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                constrain(fab){
                    bottom.linkTo(parent.bottom, 40.dp)
                    end.linkTo(parent.end, 16.dp)
                }
            },
            to = constraintSet { // this: ConstraintSetScope
                constrain(header) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.top, (-88).dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                constrain(lazyColumn) {
                    top.linkTo(header.bottom, (-12).dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                constrain(fab){
                    bottom.linkTo(parent.bottom, 40.dp)
                    end.linkTo(parent.end, 16.dp)
                }
            }
        )
    }
}
