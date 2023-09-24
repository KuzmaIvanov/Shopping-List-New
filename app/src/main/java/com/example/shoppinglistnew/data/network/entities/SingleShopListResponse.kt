package com.example.shoppinglistnew.data.network.entities

import com.google.gson.annotations.SerializedName

data class SingleShopListResponse(
    val success: Boolean,
    @SerializedName("item_list") val listItems: List<ShopListItemEntity>
)