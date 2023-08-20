package com.vp.data.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.vp.data.favorites.local.FavoriteDao
import com.vp.data.favorites.local.mapper.FavoriteDataModelMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Concrete implementation of a [FavoriteRepository]
 * This relies on an internal DB to cache the data in a persistent fashion.
 */
class DefaultFavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val mapper: FavoriteDataModelMapper
) : FavoriteRepository {

    // TODO: inject the dispatcher
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun observeAll(): LiveData<List<Favorite>> {
        return favoriteDao.observeAll().map { favorites ->
            favorites.map { mapper.mapTo(it) }
        }
    }

    override fun observeById(id: String): LiveData<Favorite?> {
        return favoriteDao.observeById(id).map { it?.let { mapper.mapTo(it) } }
    }

    override fun addFavorite(favorite: Favorite) {
        repositoryScope.launch {
            favoriteDao.addFavorite(mapper.mapFrom(favorite))
        }
    }

    override fun deleteFavorite(id: String) {
        repositoryScope.launch {
            favoriteDao.deleteById(id)
        }
    }
}