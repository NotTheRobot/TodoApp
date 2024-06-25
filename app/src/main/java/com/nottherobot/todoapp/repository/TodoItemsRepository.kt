package com.nottherobot.todoapp.repository

import com.nottherobot.todoapp.models.ui.TodoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoItemsRepository(
){
    private val repositoryScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val src = mutableListOf<TodoItem>()

    private val _todoItems = MutableStateFlow<List<TodoItem>>(mutableListOf())
    val todoItems: StateFlow<List<TodoItem>>
        get() = _todoItems.asStateFlow()

    init {
        repositoryScope.launch {
            src.addAll(generateItems(30))
            _todoItems.update { src.toList() }
        }
    }

    fun addTodoItem(item: TodoItem){
        src.add(item)
        _todoItems.update { src.toList() }
    }

    fun updateTodoItem(item: TodoItem) {
        val index = src.indexOfFirst{ it.id == item.id }
        if(index == -1){
            throw Exception("No such element")
        }
        src[index] = item
        _todoItems.update { src.toList() }
    }

    fun removeTodoItem(item: TodoItem) {
        src.remove(item)
        _todoItems.update { src.toList() }
    }
}
