package com.nottherobot.todoapp.repository

import com.nottherobot.todoapp.ui.models.TodoItem

sealed interface RepositoryResult {
    data class Success(
        val todoItems: List<TodoItem>
    ) : RepositoryResult

    data class UnknownFailure(
        val e: Exception
    ) : RepositoryResult

    data class OnlyConnectionFailure(
        val todoItems: List<TodoItem>,
        val connectionException: Exception
    ) : RepositoryResult

    data class OnlyDatabaseFailure(
        val todoItems: List<TodoItem>,
        val databaseException: Exception
    ) : RepositoryResult
}
