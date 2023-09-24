package com.example.shoppinglistnew.data.network.mappers

import com.example.shoppinglistnew.data.network.entities.ShopListEntity
import com.example.shoppinglistnew.data.network.entities.ShopListItemEntity
import com.example.shoppinglistnew.domain.models.ShopList
import com.example.shoppinglistnew.domain.models.ShopListItem
import javax.inject.Inject

class ShopListMapper @Inject constructor() {

    fun mapToDomain(shopListEntity: ShopListEntity): ShopList = with(shopListEntity) {
        ShopList(
            id = id,
            name = name,
            date = date,
        )
    }

    fun mapToDomain(shopListItemEntity: ShopListItemEntity): ShopListItem =
        with(shopListItemEntity) {
            ShopListItem(
                id = id,
                name = name,
                quantity = quantity,
                isCrossed = isCrossed
            )
        }
}