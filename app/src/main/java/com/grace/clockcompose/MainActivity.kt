package com.grace.clockcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grace.clockcompose.ui.theme.ClockComposeTheme
import kotlinx.coroutines.delay
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClockComposeTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ClockScreen()
                }
            }
        }
    }
}

data class Time(val hours: Int, val minutes: Int, val seconds: Int)

@Composable
fun ClockScreen() {
    fun currentTime(): Time {
        val cal = Calendar.getInstance()
        return Time(
            hours = cal.get(Calendar.HOUR_OF_DAY),
            minutes = cal.get(Calendar.MINUTE),
            seconds = cal.get(Calendar.SECOND),
        )
    }

    var time by remember { mutableStateOf(currentTime()) }

    LaunchedEffect(0) { // 3
        while (true) {
            time = currentTime()
            delay(1000)
        }
    }

    Clock(time)
}

@Composable
fun Clock(time: Time) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val padding = Modifier.padding(horizontal = 3.dp)

        NumberColumn(0..2, time.hours / 10, padding)
        NumberColumn(0..9, time.hours % 10, padding)

        Spacer(Modifier.size(20.dp))

        NumberColumn(0..5, time.minutes / 10, padding)
        NumberColumn(0..9, time.minutes % 10, padding)

        Spacer(Modifier.size(20.dp))

        NumberColumn(0..5, time.seconds / 10, padding)
        NumberColumn(0..9, time.seconds % 10, padding)
    }
}

@Composable
fun NumberColumn(
    range: IntRange,
    current: Int,
    modifier: Modifier = Modifier
) {
    val size = 40.dp

    val mid = (range.last - range.first) / 2f
    val reset = current == range.first
    val offset by animateDpAsState(
        targetValue = size * (mid - current),
        animationSpec = if (reset) {
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow,
            )
        } else {
            tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing,
            )
        }
    )
    Column(
        modifier = modifier
            .offset(y = offset)
            .clip(RoundedCornerShape(percent = 25))
    ) {
        range.forEach { num ->
            Number(value = num, active = num == current, Modifier.size(size))
        }
    }
}

@Composable
fun Number(value: Int, active: Boolean, modifier: Modifier = Modifier) {
    val backgroundColor by animateColorAsState(
        if (active) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.primaryVariant
        }
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(backgroundColor)
    ) {
        Text(
            text = value.toString(),
            fontSize = 20.sp,
            color = Color.White
        )
    }
}