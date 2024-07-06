package com.nottherobot.todoapp.api.models

import com.google.gson.annotations.SerializedName

data class TodoItemRequestDTO(
    @SerializedName("element")
    val todoItem: TodoItemDTO,
)
