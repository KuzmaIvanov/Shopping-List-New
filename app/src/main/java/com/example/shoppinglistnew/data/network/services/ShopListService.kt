package com.example.shoppinglistnew.data.network.services

import com.example.shoppinglistnew.data.network.entities.ShopListsResponse
import com.example.shoppinglistnew.data.network.entities.SingleShopListResponse
import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ShopListService {

    @GET("GetAllMyShopLists")
    suspend fun getAllMyShopLists(
        @Query("key") key: String
    ): ShopListsResponse

    @POST("CreateShoppingList")
    suspend fun createShopList(
        @Query("key") key: String,
        @Query("name") name: String
    ): JsonObject

    @POST("RemoveShoppingList")
    suspend fun removeShopList(
        @Query("list_id") listId: Int
    )

    @GET("GetShoppingList")
    suspend fun getShopList(
        @Query("list_id") listId: Int
    ): SingleShopListResponse

    @POST("CrossItOff")
    suspend fun crossOffShopListItem(
        @Query("id") itemId: Int
    )

    @POST("RemoveFromList")
    suspend fun removeFromList(
        @Query("list_id") listId: Int,
        @Query("item_id") itemId: Int
    )

    @POST("AddToShoppingList")
    suspend fun addToShopList(
        @Query("id") listId: Int,
        @Query("value") name: String,
        @Query("n") quantity: Int
    )
}