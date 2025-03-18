package com.rey.barelauncher.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun BottomDrawerSheet(
    drawerState: SwipeableState<DrawerValue>,
    content: @Composable () -> Unit
) {
    val screenHeight = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }

    // Ensure the drawer starts fully off-screen when closed
    val anchors = mapOf(
        (screenHeight + 100f) to DrawerValue.Closed,  // Fully off-screen
        0f to DrawerValue.Open                       // Fully visible on swipe up
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
        // Drawer content positioned off-screen when closed
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(0, drawerState.offset.value.roundToInt())
                }
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
        ) {
            content()
        }

        // Swipe indicator should only appear when swiping starts (optional)
        if (drawerState.offset.value < screenHeight) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                HorizontalDivider(
                    modifier = Modifier.width(40.dp),
                    thickness = 4.dp,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}