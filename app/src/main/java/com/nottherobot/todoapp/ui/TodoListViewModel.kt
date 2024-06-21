package com.nottherobot.todoapp.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nottherobot.todoapp.MainApplication
import com.nottherobot.todoapp.models.ui.TodoItem
import com.nottherobot.todoapp.repository.TodoItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class TodoListViewModel(
    private val repository: TodoItemsRepository
): ViewModel() {

    var todoList = repository.todoItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = listOf()
        )

    val doneTasksCount = repository.todoItemsDone
    val isShowDoneState = mutableStateOf<Boolean>(true)

    private fun updateTask(item: TodoItem){
        repository.updateTodoItem(item)
    }

    fun removeTask(item: TodoItem){
        repository.removeTodoItem(item)
    }

    fun onCheckboxClicked(item: TodoItem, isDone: Boolean){
        val newItem = item.copy(isDone = isDone)
        updateTask(newItem)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer{
                val rep = (this[APPLICATION_KEY] as MainApplication).repository
                TodoListViewModel(rep)
            }
        }
    }
}
