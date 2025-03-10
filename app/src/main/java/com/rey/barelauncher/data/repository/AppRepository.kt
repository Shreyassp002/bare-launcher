package com.rey.barelauncher.data.repository

import android.annotation.SuppressLint
import android.health.connect.datatypes.AppInfo
import com.rey.barelauncher.data.database.AppDatabase
import com.rey.barelauncher.data.database.FavoriteAppDao
import com.rey.barelauncher.data.model.FavoriteAppEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppRepository(private val favoriteAppDao: FavoriteAppDao) {
    private val packageManager = AppCompatApplication.instance.packageManager

    fun getAllFavorites(): Flow<List<AppInfo>> {
        return favoriteAppDao.getAllFavorites().map { entities ->
            entities.mapNotNull { entity ->
                try {
                    val intent = packageManager.getLaunchIntentForPackage(entity.packageName)
                    val resolveInfo = intent?.let {
                        packageManager.resolveActivity(it, 0)
                    }

                    resolveInfo?.let {
                        AppInfo(
                            name = entity.appName,
                            packageName = entity.packageName,
                            icon = it.loadIcon(packageManager)
                        )
                    }
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    @SuppressLint("NewApi")
    suspend fun addFavorite(app: AppInfo) {
        app.name?.let {
            FavoriteAppEntity(
                packageName = app.packageName,
                appName = it
            )
        }?.let {
            favoriteAppDao.addFavorite(
                it
            )
        }
    }

    suspend fun removeFavorite(packageName: String) {
        favoriteAppDao.removeFavorite(packageName)
    }

    suspend fun isFavorite(packageName: String): Boolean {
        return favoriteAppDao.isFavorite(packageName)
    }

    companion object {
        @Volatile
        private var INSTANCE: AppRepository? = null

        fun getInstance(): AppRepository {
            return INSTANCE ?: synchronized(this) {
                val database = AppDatabase.getDatabase(AppCompatApplication.instance)
                val instance = AppRepository(database.favoriteAppDao())
                INSTANCE = instance
                instance
            }
        }
    }
}
