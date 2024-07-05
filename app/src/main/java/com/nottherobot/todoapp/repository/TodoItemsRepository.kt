package com.nottherobot.todoapp.repository

import android.content.SharedPreferences
import com.nottherobot.todoapp.api.TodoService
import com.nottherobot.todoapp.api.models.TodoItemRequestDTO
import com.nottherobot.todoapp.api.models.TodoListRequestDTO
import com.nottherobot.todoapp.api.models.toTodoItemDTO
import com.nottherobot.todoapp.ui.models.TodoItem
import com.nottherobot.todoapp.ui.models.toTodoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

class TodoItemsRepository(
    private val service: TodoService,
    private val repositoryPref: SharedPreferences
) {
    private val repositoryScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private val src = mutableListOf<TodoItem>()
    private val _resultFlow = MutableStateFlow<RepositoryResult>(
        RepositoryResult.Success(
            mutableListOf()
        )
    )
    val resultFlow: StateFlow<RepositoryResult>
        get() = _resultFlow.asStateFlow()

    private var revision: AtomicInteger = AtomicInteger(-1)

    init {
        repositoryScope.launch {
            revision.set(repositoryPref.getInt("revision", 1))
            patchList()
            fetchList()
        }
    }

    suspend fun addTodoItem(item: TodoItem) {
        try {
            val response = service.addTodoItem(
                revision.toString(),
                TodoItemRequestDTO(todoItem = item.toTodoItemDTO())
            )
            updateRevision(response.revision)
            src.add(response.todoItem.toTodoItem())
            _resultFlow.update { RepositoryResult.Success(src) }
        } catch (e: Exception) {
            src.add(item)
            _resultFlow.update { RepositoryResult.OnlyConnectionFailure(src, e) }
        }
    }

    suspend fun updateTodoItem(item: TodoItem) {
        try {
            val response = service.updateTodoItem(
                revision.toString(),
                item.id,
                TodoItemRequestDTO(item.toTodoItemDTO())
            )
            updateRevision(response.revision)
            val newItem = response.todoItem.toTodoItem()
            val index = src.indexOfFirst { it.id == newItem.id }
            if (index == -1) {
                src.add(newItem)
            } else {
                src[index] = newItem
            }
            _resultFlow.update { RepositoryResult.Success(src) }
        } catch (e: Exception) {
            val index = src.indexOfFirst { it.id == item.id }
            if (index == -1) {
                src.add(item)
            } else {
                src[index] = item
            }
            _resultFlow.update { RepositoryResult.OnlyConnectionFailure(src, e) }
        }
    }

    suspend fun removeTodoItem(item: TodoItem) {
        try {
            val response = service.deleteTodoItem(
                revision.toString(),
                item.id,
                TodoItemRequestDTO(item.toTodoItemDTO())
            )
            updateRevision(response.revision)
            src.remove(response.todoItem.toTodoItem())
            _resultFlow.update { RepositoryResult.Success(src) }
        } catch (e: Exception) {
            src.remove(item)
            _resultFlow.update { RepositoryResult.OnlyConnectionFailure(src, e) }
        }
    }

    private suspend fun fetchList() {
        try {
            val response = service.getTodoList()
            updateRevision(response.revision)
            src.addAll(response.todoList.map { it.toTodoItem() })
            _resultFlow.update { RepositoryResult.Success(src) }
        } catch (e: Exception) {
            _resultFlow.update { RepositoryResult.OnlyConnectionFailure(src, e) }
        }
    }

    private suspend fun patchList() {
        try {
            val response = service.patchTodoList(
                revision.toString(),
                TodoListRequestDTO(src.map { it.toTodoItemDTO() })
            )
            updateRevision(response.revision)
            src.addAll(response.todoList.map { it.toTodoItem() })
            _resultFlow.update { RepositoryResult.Success(src) }
        } catch (e: Exception) {
            _resultFlow.update { RepositoryResult.OnlyConnectionFailure(src, e) }
        }
    }

    private fun updateRevision(newRevision: Int) {
        revision.set(newRevision)
        repositoryPref
            .edit()
            .putInt("revision", revision.get())
            .apply()
    }
}
