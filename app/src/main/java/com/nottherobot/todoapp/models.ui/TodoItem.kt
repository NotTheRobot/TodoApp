package com.nottherobot.todoapp.models.ui

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
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
    Default{
        override val order = 1
        override val stringRes: Int
            get() = R.string.none
        override val colorRes: Int
            get() = R.color.label_primary
    },
    Low{
        override val order = 0
        override val stringRes: Int
            get() = R.string.low
        override val colorRes: Int
            get() = R.color.label_primary
    },
    High{
        override val order = 2
        override val stringRes: Int
            get() = R.string.high
        override val colorRes: Int
            get() = R.color.red
    };


    abstract val order: Int
    @get:StringRes
    abstract val stringRes: Int
    @get:ColorRes
    abstract val colorRes: Int
}
