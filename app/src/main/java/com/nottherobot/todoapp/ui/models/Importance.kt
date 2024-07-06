package com.nottherobot.todoapp.ui.models

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.nottherobot.todoapp.R

enum class Importance {
    Default {
        override val order = 1
        override val transferName: String
            get() = "basic"
        override val stringRes: Int
            get() = R.string.none
        override val colorRes: Int
            get() = R.color.label_primary
    },
    Low {
        override val order = 0
        override val transferName: String
            get() = "low"
        override val stringRes: Int
            get() = R.string.low
        override val colorRes: Int
            get() = R.color.label_primary
    },
    High {
        override val order = 2
        override val transferName: String
            get() = "important"
        override val stringRes: Int
            get() = R.string.high
        override val colorRes: Int
            get() = R.color.red
    };

    abstract val order: Int
    abstract val transferName: String

    @get:StringRes
    abstract val stringRes: Int

    @get:ColorRes
    abstract val colorRes: Int

    companion object {
        fun getFromTransferName(transferName: String): Importance {
            return when (transferName) {
                "basic" -> Default
                "low" -> Low
                "important" -> High
                else -> Default
            }
        }
    }
}
