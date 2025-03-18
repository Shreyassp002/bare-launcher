package com.rey.barelauncher.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.rememberSwipeableState
import com.rey.barelauncher.ui.components.BottomDrawerSheet
import com.rey.barelauncher.ui.components.DigitalClock
import com.rey.barelauncher.ui.screens.appdrawer.AppDrawerContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun MainScreen() {
    val drawerState = rememberSwipeableState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        // Clock screen background - always visible
        DigitalClock()

        // App drawer sheet - completely invisible when closed
        BottomDrawerSheet(
            drawerState = drawerState,
            content = {
                AppDrawerContent(
                    onCloseDrawer = {
                        scope.launch {
                            drawerState.animateTo(DrawerValue.Closed)
                        }
                    }
                )
            }
        )
    }
}

