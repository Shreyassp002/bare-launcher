package com.rey.barelauncher.ui.components

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.BatteryManager
import android.widget.DigitalClock
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.cos
import kotlin.math.roundToInt
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


@Composable
fun DigitalClock(modifier: Modifier = Modifier) {
    val currentTime = remember { mutableStateOf("") }
    val currentAmPm = remember { mutableStateOf("") }
    val currentDate = remember { mutableStateOf("") }
    val batteryPct by batteryPercentage()

    LaunchedEffect(Unit) {
        while (true) {
            val cal = Calendar.getInstance()
            currentTime.value = SimpleDateFormat("hh:mm", Locale.getDefault()).format(cal.time)
            currentAmPm.value = SimpleDateFormat("a", Locale.getDefault()).format(cal.time)
            currentDate.value = SimpleDateFormat("EEE, MMM dd", Locale.getDefault()).format(cal.time)
            delay(1000L)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Battery Percentage Circular Indicator
        Box(contentAlignment = Alignment.Center) {
            // Circular Progress Indicator for Battery
            Canvas(modifier = Modifier.size(220.dp)) {
                val strokeWidth = 5.dp.toPx()
                drawArc(
                    color = Color.DarkGray,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                drawArc(
                    color = Color(0xFF4CAF50),
                    startAngle = -90f,
                    sweepAngle = 360 * (batteryPct / 100f),
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            // Clock and Date in Center
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currentTime.value,
                        color = Color.White,
                        fontSize = 52.sp, // Smaller size for modern look
                        fontWeight = FontWeight.ExtraLight,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = currentAmPm.value,
                        color = Color.Gray,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(start = 6.dp, bottom = 8.dp)
                    )
                }

                Text(
                    text = currentDate.value,
                    color = Color(0xFF4CAF50),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = "$batteryPct%",
                    color = Color.Gray.copy(alpha=0.6f),
                    fontSize=14.sp,
                    modifier=Modifier.padding(top=2.dp)
                )
            }
        }
    }
}


@Composable
fun batteryPercentage(context: Context = LocalContext.current): State<Int> {
    val batteryPct = remember { mutableIntStateOf(100) }

    DisposableEffect(Unit) {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }

        batteryStatus?.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            batteryPct.intValue = (level * 100 / scale.toFloat()).roundToInt()
        }

        onDispose { }
    }

    return batteryPct
}
