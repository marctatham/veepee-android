package com.vp.data.favorites.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Internal model used to represent a movie stored locally in a Room database.
 * This is used inside the data layer only.
 */
@Entity(
    tableName = "favorite"
)
data class FavoriteDataModel(
    @PrimaryKey val id: String,
    var title: String,
    var posterUrl: String,
)
