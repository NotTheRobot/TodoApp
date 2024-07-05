package com.nottherobot.todoapp.ui.screens.todolist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.layoutId
import com.nottherobot.todoapp.R
import com.nottherobot.todoapp.repository.generateItems
import com.nottherobot.todoapp.ui.models.TodoItem
import com.nottherobot.todoapp.ui.theme.AppTheme

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
            .padding(start = 8.dp, end = 8.dp, bottom = 60.dp)
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

@Preview(showBackground = true)
@Composable
fun TodoLazyListPreview() {
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
