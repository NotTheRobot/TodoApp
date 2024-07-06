package com.nottherobot.todoapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.nottherobot.todoapp.api.InstanceAPI
import com.nottherobot.todoapp.api.TodoService
import com.nottherobot.todoapp.repository.TodoItemsRepository

class MainApplication: Application() {
    private val repositoryPref: SharedPreferences by lazy {
        this.getSharedPreferences(
            "repositoryPref",
            Context.MODE_PRIVATE
        )
    }
    private val service: TodoService by lazy { InstanceAPI.createService(TodoService::class.java) }
    val repository: TodoItemsRepository by lazy { TodoItemsRepository(service, repositoryPref) }
}
