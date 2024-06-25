package com.nottherobot.todoapp.ui.screens.todolist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nottherobot.todoapp.R
import com.nottherobot.todoapp.models.ui.Importance
import com.nottherobot.todoapp.models.ui.TodoItem
import com.nottherobot.todoapp.ui.theme.AppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun TodoListItem(
    item: TodoItem,
    textWidth: Dp,
    onCheckboxClick: (TodoItem, Boolean) -> Unit,
    onItemClick: (String) -> Unit
) {
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
            } else if (item.importance == Importance.High) {
                AppTheme.colors.red
            } else {
                AppTheme.colors.supportSeparator
            }
            val checkboxDrawableId = if (item.isDone) {
                R.drawable.checked_checkbox
            } else if (item.importance == Importance.High) {
                R.drawable.unchecked_checkbox_high_prior
            } else {
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
                if (item.importance != Importance.Default) {
                    Image(
                        painter = painterResource(
                            id = if (item.importance == Importance.High) R.drawable.high_priority else R.drawable.low_priority
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(if (item.importance == Importance.High) AppTheme.colors.red else AppTheme.colors.gray)
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
