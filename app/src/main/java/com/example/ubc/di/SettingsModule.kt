package com.example.ubc.di

import com.example.ubc.items.ItemsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SettingsModule {
    @Provides
    fun provideItemsService() : ItemsService = ItemsService()
}