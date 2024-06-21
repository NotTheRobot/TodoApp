package com.nottherobot.todoapp.repository

import androidx.compose.runtime.mutableIntStateOf
import com.nottherobot.todoapp.models.ui.Importance
import com.nottherobot.todoapp.models.ui.TodoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

class TodoItemsRepository(
){
    private val currentContext: CoroutineScope = CoroutineScope(Dispatchers.Default)

    private val src = mutableListOf<TodoItem>()
    val todoItems = MutableStateFlow<List<TodoItem>>(mutableListOf())

    val todoItemsDone = mutableIntStateOf(0)

    init {
        currentContext.launch {
            src.addAll(generateItems())
            todoItemsDone.intValue = src.count { it.isDone }
            todoItems.value = src.sortedByImportance()
        }
    }

    fun addTodoItem(item: TodoItem){
        src.add(item)
        todoItems.value = src.sortedByImportance()
    }

    fun updateTodoItem(item: TodoItem) {
        if (item.isDone) {
            todoItemsDone.intValue++
        } else {
            todoItemsDone.intValue--
        }
        var i = 0
        while (i < src.size) {
            if (src[i].id == item.id) {
                src[i] = item
                break
            }
            i++
        }
        if(i == src.size){
            throw Exception("No such element")
        }
        todoItems.value = src.sortedByImportance()
    }

    fun removeTodoItem(item: TodoItem) {
        if(item.isDone){
            todoItemsDone.intValue--
        }
        src.remove(item)
        todoItems.value = src.sortedByImportance()
    }

    fun List<TodoItem>.sortedByImportance(): List<TodoItem>{
        return this.sortedByDescending { it.importance.order }
    }
    private fun generateItems(): MutableList<TodoItem> {
        val text = listOf(
            "маленький текст",
            """очень большой текст очень большой текст очень большой текст очень большой текст 
                |очень большой текст очень большой текст очень большой текст очень большой текст
                |очень большой текст очень большой текст очень большой текст очень большой текст
                |очень большой текст очень большой текст очень большой текст очень большой текст
            """.trimMargin()
        )
        val importance = listOf(Importance.Low, Importance.High, Importance.Default)
        val today = LocalDate.now()
        val deadline = LocalDate.now().also { it.plusDays(2L) }
        val modDate = LocalDate.now().also { it.plusDays(1L) }

        val lst = mutableListOf<TodoItem>()
        repeat(30) {
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
}
