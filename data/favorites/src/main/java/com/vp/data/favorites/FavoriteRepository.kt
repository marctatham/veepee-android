package com.vp.data.favorites

import androidx.lifecycle.LiveData

interface FavoriteRepository {

    fun observeAll(): LiveData<List<Favorite>>

    fun observeById(id: String): LiveData<Favorite>

    suspend fun addFavorite(favorite: Favorite)

    suspend fun delete(favorite: Favorite): Int
}