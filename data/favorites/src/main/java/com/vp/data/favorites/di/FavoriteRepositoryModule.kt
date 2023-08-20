package com.vp.data.favorites.di

import com.vp.data.favorites.DefaultFavoriteRepository
import com.vp.data.favorites.FavoriteRepository
import com.vp.data.favorites.local.FavoriteDao
import com.vp.data.favorites.local.mapper.FavoriteDataModelMapper
import dagger.Module
import dagger.Provides

@Module
class FavoriteRepositoryModule {

    @Provides
    fun provideFavoriteRepository(
        favoriteDao: FavoriteDao,
        mapper: FavoriteDataModelMapper
    ): FavoriteRepository {
        return DefaultFavoriteRepository(favoriteDao, mapper)
    }
}