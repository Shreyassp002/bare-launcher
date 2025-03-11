package com.rey.barelauncher.ui.screens.appdrawer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rey.barelauncher.data.model.AppInfo
import com.rey.barelauncher.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AppListViewModel : ViewModel() {
    private val _appList = MutableStateFlow<List<AppInfo>>(emptyList())
    val appList: StateFlow<List<AppInfo>> = _appList

    private val _favoriteApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val favoriteApps: StateFlow<List<AppInfo>> = _favoriteApps

    private val repository = AppRepository.getInstance()

    init {
        viewModelScope.launch {
            repository.getAllFavorites().collect { favorites ->
                _favoriteApps.value = favorites
            }
        }
    }

    @SuppressLint("NewApi", "QueryPermissionsNeeded")
    fun loadInstalledApps(context: Context) {
        viewModelScope.launch {
            val packageManager = context.packageManager
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)

            val apps = packageManager.queryIntentActivities(intent, 0)
                .map { resolveInfo ->
                    AppInfo(
                        name = resolveInfo.loadLabel(packageManager).toString(),
                        packageName = resolveInfo.activityInfo.packageName,
                        icon = resolveInfo.loadIcon(packageManager)
                    )
                }
                .sortedBy { it.name }

            _appList.value = apps
        }
    }

    @SuppressLint("NewApi")
    fun toggleFavorite(app: AppInfo) {
        viewModelScope.launch {
            val isFavorite = _favoriteApps.value.any { it.packageName == app.packageName }

            if (isFavorite) {
                repository.removeFavorite(app.packageName)
            } else {
                repository.addFavorite(app)
            }
        }
    }
}
