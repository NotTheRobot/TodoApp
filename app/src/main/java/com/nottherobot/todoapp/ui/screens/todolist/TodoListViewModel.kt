package com.nottherobot.todoapp.ui.screens.todolist

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nottherobot.todoapp.MainApplication
import com.nottherobot.todoapp.repository.RepositoryResult
import com.nottherobot.todoapp.repository.TodoItemsRepository
import com.nottherobot.todoapp.ui.models.TodoItem
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoListViewModel(
    private val repository: TodoItemsRepository
) : ViewModel() {

    val errorText = mutableStateOf("")
    val todoList = repository.resultFlow
        .map { result ->
            when (result) {
                is RepositoryResult.Success -> {
                    result.todoItems.countDoneAndInitialize()
                    result.todoItems.sortedByImportance().await()
                }

                is RepositoryResult.OnlyConnectionFailure -> {
                    showError(result.connectionException.toString())
                    result.todoItems.countDoneAndInitialize()
                    result.todoItems.sortedByImportance().await()
                }

                else -> {
                    showError("я не знаю что происходит если бы я знал, что происходит я не знаубврлврлврлврлврл")
                    listOf<TodoItem>()
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = listOf()
        )
    val doneTasksCount = mutableIntStateOf(0)
    val isShowDone = mutableStateOf<Boolean>(true)

    private fun updateTask(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateTodoItem(item)
            } catch (e: Exception) {
                showError(e.localizedMessage)
            }
        }
    }

    fun removeTask(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeTodoItem(item)
        }
    }

    fun onCheckboxClicked(item: TodoItem, isDone: Boolean) {
        val newItem = item.copy(isDone = isDone)
        updateTask(newItem)
    }

    private fun List<TodoItem>.countDoneAndInitialize() {
        viewModelScope.launch(Dispatchers.IO) {
            doneTasksCount.intValue = this@countDoneAndInitialize.count { it.isDone }
        }
    }

    private fun List<TodoItem>.sortedByImportance(): Deferred<List<TodoItem>> {
        return viewModelScope.async(Dispatchers.IO) {
            this@sortedByImportance.sortedByDescending { it.importance.order }
        }
    }

    private fun showError(text: String?) {
        viewModelScope.launch {
            errorText.value = text ?: "Unknown Error"
            delay(5_000)
            errorText.value = ""
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val rep = (this[APPLICATION_KEY] as MainApplication).repository
                TodoListViewModel(rep)
            }
        }
    }
}
