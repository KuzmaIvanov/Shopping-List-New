package com.example.shoppinglistnew.data.network.services

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthenticationService {

    @GET("CreateTestKey")
    suspend fun getKey(): Response<String>

    @GET("Authentication")
    suspend fun getAuthentication(
        @Query("key") key: String
    ): JsonObject
}