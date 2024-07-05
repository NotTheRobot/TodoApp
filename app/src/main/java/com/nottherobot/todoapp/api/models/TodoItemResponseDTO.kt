package com.nottherobot.todoapp.api.models

import com.google.gson.annotations.SerializedName

data class TodoItemResponseDTO(
    @SerializedName("status")
    val status: String,
    @SerializedName("element")
    val todoItem: TodoItemDTO,
    @SerializedName("revision")
    val revision: Int
)
