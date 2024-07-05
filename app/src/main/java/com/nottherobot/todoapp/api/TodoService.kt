package com.nottherobot.todoapp.api

import com.nottherobot.todoapp.api.models.TodoItemRequestDTO
import com.nottherobot.todoapp.api.models.TodoItemResponseDTO
import com.nottherobot.todoapp.api.models.TodoListRequestDTO
import com.nottherobot.todoapp.api.models.TodoListResponseDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface TodoService {
    @GET("list")
    suspend fun getTodoList(): TodoListResponseDTO

    @PATCH("list")
    suspend fun patchTodoList(
        @Header(InstanceAPI.RevisionHeaderKey) revision: String,
        @Body todoList: TodoListRequestDTO
    ): TodoListResponseDTO

    @GET("list/")
    suspend fun getTodoItem(@Query("id") id: String): TodoItemResponseDTO

    @POST("list")
    suspend fun addTodoItem(
        @Header(InstanceAPI.RevisionHeaderKey) revision: String,
        @Body todoItem: TodoItemRequestDTO
    ): TodoItemResponseDTO

    @PUT("list/")
    suspend fun updateTodoItem(
        @Header(InstanceAPI.RevisionHeaderKey) revision: String,
        @Query("id") id: String,
        @Body todoItem: TodoItemRequestDTO
    ): TodoItemResponseDTO

    @DELETE("list/")
    suspend fun deleteTodoItem(
        @Header(InstanceAPI.RevisionHeaderKey) revision: String,
        @Query("id") id: String,
        @Body todoItem: TodoItemRequestDTO
    ): TodoItemResponseDTO
}
