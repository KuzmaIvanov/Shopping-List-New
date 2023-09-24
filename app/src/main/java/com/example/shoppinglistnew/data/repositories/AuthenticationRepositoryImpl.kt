package com.example.shoppinglistnew.data.repositories

import com.example.shoppinglistnew.data.network.services.AuthenticationService
import com.example.shoppinglistnew.domain.repositories.AuthenticationRepository
import com.google.gson.JsonObject
import retrofit2.Response
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationService: AuthenticationService
) : AuthenticationRepository {

    override suspend fun getKey(): Response<String> {
        return authenticationService.getKey()
    }

    override suspend fun getAuthentication(key: String): JsonObject {
        return authenticationService.getAuthentication(key)
    }
}