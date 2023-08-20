package com.vp.data.favorites.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vp.data.favorites.local.model.Favorite

/**
 * The Room Database that contains the Favorite table.
 */
// TODO: exportSchema should be updated to true when the implementation is ready
@Database(entities = [Favorite::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao
}
