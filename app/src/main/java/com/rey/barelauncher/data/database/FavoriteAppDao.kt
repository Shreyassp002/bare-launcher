package com.rey.barelauncher.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rey.barelauncher.data.model.FavoriteAppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteAppDao {
    @Query("SELECT * FROM favorite_apps ORDER BY appName")
    fun getAllFavorites(): Flow<List<FavoriteAppEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(app: FavoriteAppEntity)

    @Query("DELETE FROM favorite_apps WHERE packageName = :packageName")
    suspend fun removeFavorite(packageName: String)

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_apps WHERE packageName = :packageName)")
    suspend fun isFavorite(packageName: String): Boolean
}