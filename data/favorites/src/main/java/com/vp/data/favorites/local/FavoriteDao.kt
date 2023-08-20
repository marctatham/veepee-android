package com.vp.data.favorites.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vp.data.favorites.local.model.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    /**
     * Observes all favorites.
     */
    @Query("SELECT * FROM favorite")
    fun observeAll(): LiveData<List<Favorite>>

    /**
     * Observes a single favorite.
     */
    @Query("SELECT * FROM favorite WHERE id = :id")
    fun observeById(id: String): Flow<Favorite>

    /**
     * Insert a favorite in the database. If a favorite already exists, replace it.
     */
    @Insert
    suspend fun addFavorite(favorite: Favorite)

    /**
     * Delete a favorite by id.
     *
     * @return the number of items deleted from the table. This should always be 1.
     */
    @Query("DELETE FROM favorite WHERE id = :id")
    suspend fun deleteById(id: String): Int
}

