package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.vp.data.favorites.Favorite
import com.vp.data.favorites.FavoriteRepository
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    fun favorites(): LiveData<List<Favorite>> = favoriteRepository.observeAll()

}