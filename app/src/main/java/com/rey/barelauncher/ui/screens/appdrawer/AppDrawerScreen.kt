package com.rey.barelauncher.ui.screens.appdrawer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rey.barelauncher.ui.components.AppItem
import com.rey.barelauncher.utils.launchApp

@SuppressLint("NewApi")
@Composable
fun AppDrawerContent(onCloseDrawer: () -> Unit) {
    val context = LocalContext.current
    val appListViewModel = viewModel<AppListViewModel>()
    val appList by appListViewModel.appList.collectAsState()
    val favoriteApps by appListViewModel.favoriteApps.collectAsState()

    LaunchedEffect(Unit) {
        appListViewModel.loadInstalledApps(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Handle at the top of drawer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            HorizontalDivider(
                modifier = Modifier.width(40.dp),
                thickness = 4.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        }

        Text(
            text = "Apps",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Favorites section
        if (favoriteApps.isNotEmpty()) {
            Text(
                text = "Favorites",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(favoriteApps) { app ->
                    AppItem(
                        app = app,
                        isFavorite = true,
                        onAppClick = {
                            launchApp(context, app.packageName)
                            onCloseDrawer()
                        },
                        onFavoriteToggle = {
                            appListViewModel.toggleFavorite(app)
                        },
                        isCompact = true
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }

        // All apps
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(appList) { app ->
                AppItem(
                    app = app,
                    isFavorite = favoriteApps.any { it.packageName == app.packageName },
                    onAppClick = {
                        launchApp(context, app.packageName)
                        onCloseDrawer()
                    },
                    onFavoriteToggle = {
                        appListViewModel.toggleFavorite(app)
                    }
                )
            }
        }
    }
}
