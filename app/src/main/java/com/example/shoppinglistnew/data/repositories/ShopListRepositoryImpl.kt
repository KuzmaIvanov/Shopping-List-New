package com.example.shoppinglistnew.data.repositories

import com.example.shoppinglistnew.data.network.mappers.ShopListMapper
import com.example.shoppinglistnew.data.network.services.ShopListService
import com.example.shoppinglistnew.domain.models.ShopList
import com.example.shoppinglistnew.domain.models.ShopListItem
import com.example.shoppinglistnew.domain.repositories.ShopListRepository
import com.google.gson.JsonObject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShopListRepositoryImpl @Inject constructor(
    private val shopListService: ShopListService,
    private val shopListMapper: ShopListMapper
) : ShopListRepository {

    override suspend fun getAllMyShopLists(key: String): List<ShopList> {
        return shopListService.getAllMyShopLists(key).shopLists.map {
            shopListMapper.mapToDomain(it)
        }
    }

    override suspend fun createShopList(key: String, name: String): JsonObject {
        return shopListService.createShopList(key, name)
    }

    override suspend fun removeShopList(listId: Int) {
        shopListService.removeShopList(listId)
    }

    override suspend fun getShopList(listId: Int, refreshIntervalMs: Long): Flow<List<ShopListItem>> = flow {
        while (true) {
            val latestShopList = shopListService.getShopList(listId).listItems.map {
                shopListMapper.mapToDomain(it)
            }
            emit(latestShopList)
            delay(refreshIntervalMs)
        }
    }

    override suspend fun crossOffShopListItem(itemId: Int) {
        shopListService.crossOffShopListItem(itemId)
    }

    override suspend fun removeFromList(listId: Int, itemId: Int) {
        shopListService.removeFromList(listId, itemId)
    }

    override suspend fun addToShopList(listId: Int, name: String, quantity: Int) {
        shopListService.addToShopList(listId, name, quantity)
    }
}