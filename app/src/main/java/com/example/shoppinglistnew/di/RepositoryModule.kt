package com.example.shoppinglistnew.di

import com.example.shoppinglistnew.data.repositories.AuthenticationRepositoryImpl
import com.example.shoppinglistnew.data.repositories.ShopListRepositoryImpl
import com.example.shoppinglistnew.domain.repositories.AuthenticationRepository
import com.example.shoppinglistnew.domain.repositories.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindAuthenticationRepository(
        authenticationRepositoryImpl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Singleton
    @Binds
    fun bindShopListRepository(
        shopListRepositoryImpl: ShopListRepositoryImpl
    ): ShopListRepository
}