package com.example.shoppinglistnew.domain.models

data class ShopListItem(
    val id: Int,
    val name: String,
    val quantity: Int,
    val isCrossed: Boolean
)