package com.vp.data.favorites.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vp.data.favorites.local.model.FavoriteDataModel

/**
 * The Room Database that contains the Favorite table.
 */
// TODO: exportSchema should be updated to true when the implementation is ready
@Database(entities = [FavoriteDataModel::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao
}
