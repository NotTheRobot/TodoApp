package com.nottherobot.todoapp.ui.models

import com.nottherobot.todoapp.api.models.TodoItemDTO
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadlineDate: LocalDateTime?,
    val isDone: Boolean = false,
    val creationDate: LocalDateTime,
    val modificationDate: LocalDateTime
)

fun TodoItemDTO.toTodoItem() = TodoItem(
    id = this.id,
    text = this.text,
    importance = Importance.getFromTransferName(this.importance),
    deadlineDate = this.deadlineDate?.toLocalDateTime(),
    isDone = this.isDone,
    creationDate = this.creationDate.toLocalDateTime(),
    modificationDate = this.modificationDate.toLocalDateTime(),
)

fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
}
