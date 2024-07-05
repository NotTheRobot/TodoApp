package com.nottherobot.todoapp.api.models

import com.google.gson.annotations.SerializedName

data class TodoListResponseDTO(
    @SerializedName("status")
    val status: String,
    @SerializedName("list")
    val todoList: List<TodoItemDTO>,
    @SerializedName("revision")
    val revision: Int
)
