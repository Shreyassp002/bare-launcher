package com.rey.barelauncher.data.repository

import android.annotation.SuppressLint
import com.rey.barelauncher.MyApplication
import com.rey.barelauncher.data.database.AppDatabase
import com.rey.barelauncher.data.database.FavoriteAppDao
import com.rey.barelauncher.data.model.AppInfo
import com.rey.barelauncher.data.model.FavoriteAppEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppRepository(private val favoriteAppDao: FavoriteAppDao) {
    private val packageManager = MyApplication.instance.packageManager

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
        FavoriteAppEntity(
            packageName = app.packageName,
            appName = app.name
        ).let {
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
                val database = AppDatabase.getDatabase(MyApplication.instance)
                val instance = AppRepository(database.favoriteAppDao())
                INSTANCE = instance
                instance
            }
        }
    }
}
