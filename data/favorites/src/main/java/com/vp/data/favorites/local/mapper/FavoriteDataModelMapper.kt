package com.vp.data.favorites.local.mapper

import com.vp.data.favorites.Favorite
import com.vp.data.favorites.local.model.FavoriteDataModel

class FavoriteDataModelMapper {

    fun map(dataModel: FavoriteDataModel): Favorite = Favorite(
        dataModel.id,
        dataModel.title,
        dataModel.posterUrl
    )

}