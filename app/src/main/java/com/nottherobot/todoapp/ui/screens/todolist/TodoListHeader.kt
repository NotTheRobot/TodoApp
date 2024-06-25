package com.nottherobot.todoapp.ui.screens.todolist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionLayoutScope
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.layoutId
import com.nottherobot.todoapp.R
import com.nottherobot.todoapp.ui.theme.AppTheme


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
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth()
                    .background(AppTheme.colors.backPrimary)
            )
            Box(
                modifier = Modifier
                    .height(4.dp)
                    .fillMaxWidth()
                    .background(brush = Brush.verticalGradient(AppTheme.colors.shadowGradient))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListHeaderPreviewEnd() {
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
fun TodoListHeaderPreviewStart() {
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
