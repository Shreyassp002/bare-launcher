package com.rey.barelauncher.ui.components

import android.icu.util.Calendar
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalogClock(modifier: Modifier = Modifier) {
    val time = remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            time.longValue = System.currentTimeMillis()
            delay(1000L)
        }
    }

    val calendar = Calendar.getInstance().apply {
        timeInMillis = time.longValue
    }

    val hours = calendar.get(Calendar.HOUR)
    val minutes = calendar.get(Calendar.MINUTE)
    val seconds = calendar.get(Calendar.SECOND)

    Canvas(
        modifier = modifier
            .size(300.dp)
            .padding(16.dp)
    ) {
        // Draw clock face
        drawCircle(
            color = Color.White,
            radius = size.minDimension / 2,
            style = Stroke(width = 5f)
        )

        // Draw hour markers
        for (i in 0 until 12) {
            val angle = (i * 30) * (Math.PI / 180f)
            val markerLength = 20f
            val startRadius = size.minDimension / 2 - markerLength
            val endRadius = size.minDimension / 2

            val startX = (size.width / 2 + cos(angle) * startRadius).toFloat()
            val startY = (size.height / 2 + sin(angle) * startRadius).toFloat()
            val endX = (size.width / 2 + cos(angle) * endRadius).toFloat()
            val endY = (size.height / 2 + sin(angle) * endRadius).toFloat()

            drawLine(
                color = Color.White,
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 5f
            )
        }

        // Draw clock hands
        drawClockHand(hours * 30f + minutes * 0.5f, 0.5f, Color.White)
        drawClockHand(minutes * 6f, 0.7f, Color.White)
        drawClockHand(seconds * 6f, 0.9f, Color.Red)
    }
}

private fun DrawScope.drawClockHand(
    angleInDegrees: Float,
    lengthFraction: Float,
    color: Color
) {
    val angle = (angleInDegrees - 90) * (Math.PI / 180f)
    val handLength = size.minDimension / 2 * lengthFraction

    val endX = (size.width / 2 + cos(angle) * handLength).toFloat()
    val endY = (size.height / 2 + sin(angle) * handLength).toFloat()

    drawLine(
        color = color,
        start = Offset(size.width / 2, size.height / 2),
        end = Offset(endX, endY),
        strokeWidth = 4f
    )
}
