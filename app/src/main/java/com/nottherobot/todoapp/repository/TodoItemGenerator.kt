package com.nottherobot.todoapp.repository

import com.nottherobot.todoapp.models.ui.Importance
import com.nottherobot.todoapp.models.ui.TodoItem
import java.time.LocalDate
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

fun generateItems(count: Int): List<TodoItem> {
    val text = listOf(
        "маленький текст",
        """очень большой текст очень большой текст очень большой текст очень большой текст 
                |очень большой текст очень большой текст очень большой текст очень большой текст
                |очень большой текст очень большой текст очень большой текст очень большой текст
                |очень большой текст очень большой текст очень большой текст очень большой текст
            """.trimMargin()
    )
    val importance = Importance.entries
    val today = LocalDate.now()
    val deadline = today.plusDays(2L)
    val modDate = today.plusDays(1L)

    val lst = mutableListOf<TodoItem>()
    repeat(count) {
        lst.add(
            TodoItem(
                UUID.randomUUID().toString(),
                text[abs(Random.nextInt() % 2)],
                importance[abs(Random.nextInt()) % 3],
                if (it % 2 == 0) deadline else null,
                Random.nextInt() % 2 == 0,
                today,
                if (it % 2 == 0) modDate else null
            )
        )
    }
    return lst
}
