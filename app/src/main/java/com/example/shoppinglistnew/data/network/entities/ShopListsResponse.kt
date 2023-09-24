package com.example.shoppinglistnew.data.network.entities

import com.google.gson.annotations.SerializedName

data class ShopListsResponse(
    @SerializedName("shop_list") val shopLists: List<ShopListEntity>,
    val success: Boolean
)