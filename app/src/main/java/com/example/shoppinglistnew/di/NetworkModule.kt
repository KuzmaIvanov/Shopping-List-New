package com.example.shoppinglistnew.di

import com.example.shoppinglistnew.BuildConfig
import com.example.shoppinglistnew.data.network.services.AuthenticationService
import com.example.shoppinglistnew.data.network.services.ShopListService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideShopListService(
        retrofit: Retrofit
    ): ShopListService {
        return retrofit.create(ShopListService::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthenticationService(
        retrofit: Retrofit
    ): AuthenticationService {
        return retrofit.create(AuthenticationService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(
        gson: Gson
    ): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }
}