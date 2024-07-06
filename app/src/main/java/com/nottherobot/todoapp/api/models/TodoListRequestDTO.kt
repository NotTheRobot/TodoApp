package com.nottherobot.todoapp.api.models

import com.google.gson.annotations.SerializedName

data class TodoListRequestDTO(
    @SerializedName("list")
    val todoList: List<TodoItemDTO>
)
