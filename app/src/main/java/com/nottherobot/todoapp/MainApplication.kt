package com.nottherobot.todoapp

import android.app.Application
import com.nottherobot.todoapp.repository.TodoItemsRepository

class MainApplication: Application() {
    val repository: TodoItemsRepository by lazy{ TodoItemsRepository() }
}
