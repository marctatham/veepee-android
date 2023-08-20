package com.vp.data.favorites

import androidx.lifecycle.LiveData

interface FavoriteRepository {

    fun observeAll(): LiveData<List<Favorite>>

    fun observeById(id: String): LiveData<Favorite?>

    fun addFavorite(favorite: Favorite)

    fun deleteFavorite(id: String)
}