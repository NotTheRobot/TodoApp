package com.nottherobot.todoapp.repository

import com.nottherobot.todoapp.models.ui.Importance
import com.nottherobot.todoapp.models.ui.TodoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

class TodoItemsRepository{
    private val currentContext: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private val src = mutableListOf<TodoItem>()

    private val _todoItems = MutableStateFlow<List<TodoItem>>(mutableListOf())
    val todoItems: StateFlow<List<TodoItem>>
        get() = _todoItems.asStateFlow()

    init {
        currentContext.launch {
            src.addAll(generateItems())
            _todoItems.value = src.toList()
        }
    }

    fun addTodoItem(item: TodoItem){
        src.add(item)
        _todoItems.value = src.toList()
    }

    fun updateTodoItem(item: TodoItem) {
        var i = 0
        while (i < src.size) {
            if (src[i].id == item.id) {
                src[i] = item
                break
            }
            i++
        }
        if(i == src.toList().size){
            throw Exception("No such element")
        }
        _todoItems.value = src.toList()
    }

    fun removeTodoItem(item: TodoItem) {
        src.remove(item)
        _todoItems.value = src.toList()
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
