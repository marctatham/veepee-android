package com.vp.data.favorites.local.mapper

import com.vp.data.favorites.Favorite
import com.vp.data.favorites.local.model.FavoriteDataModel
import javax.inject.Inject

class FavoriteDataModelMapper @Inject constructor() {

    fun mapTo(dataModel: FavoriteDataModel): Favorite = Favorite(
        dataModel.id,
        dataModel.title,
        dataModel.posterUrl
    )

    fun mapFrom(favorite: Favorite): FavoriteDataModel = FavoriteDataModel(
        favorite.id,
        favorite.title,
        favorite.posterUrl
    )
}