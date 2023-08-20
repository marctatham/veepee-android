package com.vp.data.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.vp.data.favorites.local.FavoriteDao
import com.vp.data.favorites.local.mapper.FavoriteDataModelMapper
import javax.inject.Inject

/**
 * Concrete implementation of a [FavoriteRepository]
 * This relies on an internal DB to cache the data in a persistent fashion.
 */
class DefaultFavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val mapper: FavoriteDataModelMapper
) : FavoriteRepository {

    override fun observeAll(): LiveData<List<Favorite>> {
        return favoriteDao.observeAll().map { favorites ->
            favorites.map { mapper.mapTo(it) }
        }
    }

    override fun observeById(id: String): LiveData<Favorite> {
        return favoriteDao.observeById(id).map { mapper.mapTo(it) }
    }

    override suspend fun addFavorite(favorite: Favorite) {
        favoriteDao.addFavorite(mapper.mapFrom(favorite))
    }

    override suspend fun delete(favorite: Favorite): Int {
        return favoriteDao.deleteById(favorite.id)
    }
}