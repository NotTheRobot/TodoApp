package com.nottherobot.todoapp.models.ui

import com.nottherobot.todoapp.R
import java.time.LocalDate

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadlineDate: LocalDate?,
    val isDone: Boolean = false,
    val creationDate: LocalDate,
    val modificationDate: LocalDate?
)

enum class Importance{
    Low{
        override val order = 0
    },
    High{
        override val order = 2
    },
    Default{
        override val order = 1
    };

    abstract val order: Int

    fun getStringId(): Int{
        return when(this){
            Importance.Default -> R.string.none
            Importance.Low -> R.string.low
            Importance.High -> R.string.high
        }
    }
}
