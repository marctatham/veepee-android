package com.vp.data.favorites.di

import android.app.Application
import androidx.room.Room
import com.vp.data.favorites.local.FavoriteDao
import com.vp.data.favorites.local.FavoriteDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): FavoriteDatabase = Room.databaseBuilder(
        application.applicationContext,
        FavoriteDatabase::class.java,
        "Favorite.db"
    ).build()


    @Provides
    fun provideFavoriteDao(database: FavoriteDatabase): FavoriteDao = database.favoriteDao()
}