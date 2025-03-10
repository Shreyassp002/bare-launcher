package com.rey.barelauncher.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.SwipeableState
import androidx.wear.compose.material.swipeable
import kotlin.math.roundToInt


@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun BottomDrawerSheet(
    drawerState: SwipeableState<androidx.compose.material3.DrawerValue>,
    content: @Composable () -> Unit
) {
    val anchors = mapOf(
        0f to DrawerValue.Closed,
        with(LocalDensity.current) {
            LocalConfiguration.current.screenHeightDp.dp.toPx()
        } to DrawerValue.Open
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .swipeable(
                state = drawerState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Vertical
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(
                        0,
                        drawerState.offset.value.roundToInt()
                    )
                }
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
        ) {
            content()
        }
    }
}

enum class DrawerValue {
    Open,
    Closed
}
