package com.example.shoppinglistnew.data.network.entities

import com.google.gson.annotations.SerializedName

data class ShopListEntity(
    @SerializedName("created") val date: String,
    val name: String,
    val id: Int
)