package com.nottherobot.todoapp.repository

import android.util.Log
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
    private val repositoryScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private val src = mutableListOf<TodoItem>()

    private val _todoItems = MutableStateFlow<List<TodoItem>>(mutableListOf())
    val todoItems: StateFlow<List<TodoItem>>
        get() = _todoItems.asStateFlow()

    init {
        repositoryScope.launch {
            Log.d("kekw", "repository thread: ${Thread.currentThread()}")
            src.addAll(generateItems(30))
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
}
