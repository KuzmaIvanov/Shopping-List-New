package com.example.shoppinglistnew.data.network.entities

import com.google.gson.annotations.SerializedName

data class ShopListItemEntity(
    val id: Int,
    val name: String,
    @SerializedName("created") val quantity: Int,
    @SerializedName("is_crossed") val isCrossed: Boolean
)