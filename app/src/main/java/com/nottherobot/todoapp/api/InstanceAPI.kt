package com.nottherobot.todoapp.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object InstanceAPI {
    const val RevisionHeaderKey = "X-Last-Known-Revision"
    private const val authHeaderKey = "Authorization"
    private const val authToken = "Bearer Estel"
    private const val baseUrl = "https://hive.mrdekk.ru/todo/"
    private const val failsGeneratorHeaderKey = "X-Generate-Fails"
    private val logging =
        HttpLoggingInterceptor().also { it.setLevel(HttpLoggingInterceptor.Level.BODY) }
    private val authInterceptor = Interceptor { chain ->
        val req = chain.request()
            .newBuilder()
            .addHeader(authHeaderKey, authToken)
            .build()
        return@Interceptor chain.proceed(req)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }
}
