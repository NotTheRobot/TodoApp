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
import com.nottherobot.todoapp.repository.RepositoryResult
import com.nottherobot.todoapp.repository.TodoItemsRepository
import com.nottherobot.todoapp.ui.models.Importance
import com.nottherobot.todoapp.ui.models.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

class EditTodoViewModel(
    private val repository: TodoItemsRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var id: String? = savedStateHandle.get<String>("itemId")
    val item = mutableStateOf<TodoItem?>(null)
    val text = mutableStateOf("")
    val importance = mutableStateOf(Importance.Default)
    val deadlineDate = mutableStateOf<LocalDateTime?>(null)
    private val modificationDate = mutableStateOf<LocalDateTime?>(null)

    init {
        viewModelScope.launch {
            repository.resultFlow
                .map { result ->
                    when (result) {
                        is RepositoryResult.Success -> {
                            result.todoItems.find { it.id == id }
                        }

                        else -> {
                            null
                        }
                    }
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
            creationDate = item.value?.creationDate ?: LocalDateTime.now(),
            modificationDate = LocalDateTime.now()
        )
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (item.value == null) {
                    repository.addTodoItem(newItem)
                } else {
                    repository.updateTodoItem(newItem)
                }
            } catch (e: Exception) {
                // можно словить исключение только если выйдем за максимальный размер в длине массива (почти нереально)
                // можно где-нибудь логировать исключение, но мне оно не надо
                // поэтому просто выходим из экрана и ничего не сохраняем
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
