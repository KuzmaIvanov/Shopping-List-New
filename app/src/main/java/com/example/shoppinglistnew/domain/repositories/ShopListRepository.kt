package com.example.shoppinglistnew.domain.repositories

import com.example.shoppinglistnew.domain.models.ShopList
import com.example.shoppinglistnew.domain.models.ShopListItem
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow

interface ShopListRepository {
    suspend fun getAllMyShopLists(key: String): List<ShopList>
    suspend fun createShopList(key: String, name: String): JsonObject
    suspend fun removeShopList(listId: Int)
    suspend fun getShopList(listId: Int, refreshIntervalMs: Long): Flow<List<ShopListItem>>
    suspend fun crossOffShopListItem(itemId: Int)
    suspend fun removeFromList(listId: Int, itemId: Int)
    suspend fun addToShopList(listId: Int, name: String, quantity: Int)
}