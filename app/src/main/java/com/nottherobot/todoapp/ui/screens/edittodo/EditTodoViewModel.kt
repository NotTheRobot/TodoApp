package com.nottherobot.todoapp.ui.screens.edittodo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nottherobot.todoapp.MainApplication
import com.nottherobot.todoapp.models.ui.Importance
import com.nottherobot.todoapp.models.ui.TodoItem
import com.nottherobot.todoapp.repository.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class EditTodoViewModel(
    private val repository: TodoItemsRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var id: String? = savedStateHandle.get<String>("itemId")
    val item = mutableStateOf<TodoItem?>(null)
    val text = mutableStateOf("")
    val importance = mutableStateOf(Importance.Default)
    val deadlineDate = mutableStateOf<LocalDate?>(null)
    private val modificationDate = mutableStateOf<LocalDate?>(null)

    init {
        viewModelScope.launch {
            repository.todoItems
                .map { lst ->
                    lst.find { it.id == id }
                }
                .collect {
                    item.value = it
                    if (item.value != null) {
                        text.value = item.value!!.text
                        importance.value = item.value!!.importance
                        deadlineDate.value = item.value!!.deadlineDate
                        modificationDate.value = item.value!!.modificationDate
                    }
                }
        }
    }

    fun onSaveClick() {
        val newItem = TodoItem(
            id = item.value?.id ?: (UUID.randomUUID().toString()),
            text = text.value,
            importance = importance.value,
            deadlineDate = deadlineDate.value,
            isDone = item.value?.isDone ?: false,
            creationDate = item.value?.creationDate ?: LocalDate.now(),
            modificationDate = if (item.value != null) LocalDate.now() else null
        )
        viewModelScope.launch(Dispatchers.IO) {
            if (item.value == null) {
                repository.addTodoItem(newItem)
            } else {
                repository.updateTodoItem(newItem)
            }
        }
    }

    fun onDeleteClick() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeTodoItem(item.value!!)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val rep = (this[APPLICATION_KEY] as MainApplication).repository
                val saveStateHande = createSavedStateHandle()
                EditTodoViewModel(rep, saveStateHande)
            }
        }
    }
}
