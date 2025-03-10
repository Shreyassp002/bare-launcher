package com.rey.barelauncher.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorite_apps")
data class FavoriteAppEntity(
    @PrimaryKey val packageName: String,
    val appName: String,
    // We don't store the icon drawable in the database
    // Instead, we'll fetch it from the package manager when needed
)



