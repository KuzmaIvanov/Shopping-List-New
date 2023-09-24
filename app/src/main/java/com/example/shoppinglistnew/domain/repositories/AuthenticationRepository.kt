package com.example.shoppinglistnew.domain.repositories

import com.google.gson.JsonObject
import retrofit2.Response

interface AuthenticationRepository {
    suspend fun getKey(): Response<String>
    suspend fun getAuthentication(key: String): JsonObject
}