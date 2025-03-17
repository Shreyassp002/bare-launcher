package com.rey.barelauncher.ui.screens.home

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.rememberSwipeableState

import com.rey.barelauncher.ui.components.BottomDrawerSheet
import com.rey.barelauncher.ui.screens.appdrawer.AppDrawerContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun MainScreen() {
    val drawerState = rememberSwipeableState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        // Clock screen background - always visible
        ClockScreen()

        // App drawer sheet
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

        // Swipe indicator at bottom
        if (drawerState.currentValue == DrawerValue.Closed) {
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

@Composable
fun ClockScreen(modifier: Modifier = Modifier) {
    val time = remember { mutableStateOf("") }

    // Update time every second
    LaunchedEffect(Unit) {
        while (true) {
            val cal = Calendar.getInstance()
            time.value = SimpleDateFormat("HH:mm", Locale.getDefault()).format(cal.time)
            delay(1000)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = time.value,
            color = Color.White,
            fontSize = 72.sp,
            fontWeight = FontWeight.Light
        )
    }
}