package com.nottherobot.todoapp.api.models

import android.provider.Settings
import com.google.gson.annotations.SerializedName
import com.nottherobot.todoapp.ui.models.TodoItem
import java.time.LocalDateTime
import java.time.ZoneId

data class TodoItemDTO(
    @SerializedName("id")
    val id: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("importance")
    val importance: String,
    @SerializedName("deadline")
    val deadlineDate: Long?,
    @SerializedName("done")
    val isDone: Boolean,
    @SerializedName("color")
    val color: String? = null,
    @SerializedName("created_at")
    val creationDate: Long,
    @SerializedName("changed_at")
    val modificationDate: Long,
    @SerializedName("last_updated_by")
    val lastUpdatedBy: String? = null
)

fun TodoItem.toTodoItemDTO() = TodoItemDTO(
    id = this.id,
    text = this.text,
    importance = this.importance.transferName,
    deadlineDate = this.deadlineDate?.toEpochMilli(),
    isDone = this.isDone,
    color = null,
    creationDate = this.creationDate.toEpochMilli()!!,
    modificationDate = this.modificationDate.toEpochMilli()!!,
    lastUpdatedBy = Settings.Secure.ANDROID_ID
)

fun LocalDateTime.toEpochMilli(): Long? {
    return this.toInstant(ZoneId.systemDefault().rules.getOffset(this))?.toEpochMilli()
}
