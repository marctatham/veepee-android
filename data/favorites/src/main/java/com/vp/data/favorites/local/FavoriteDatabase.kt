package com.vp.data.favorites.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vp.data.favorites.local.model.FavoriteDataModel

/**
 * The Room Database that contains the Favorite table.
 */
@Database(entities = [FavoriteDataModel::class], version = 1, exportSchema = true)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao
}
