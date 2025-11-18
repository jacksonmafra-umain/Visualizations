package com.umain.visualizations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun BatteryViz() {
    val barCount = 10
    // generate initial random levels
    val levels = remember { List(barCount) { Animatable(0f) } }

    LaunchedEffect(Unit) {
        while (true) {
            levels.forEach { anim ->
                anim.animateTo(
                    targetValue = Random.nextFloat(),
                    animationSpec =
                        tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing,
                        ),
                )
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width / (barCount + 0.5f)
        val gradient =
            Brush.verticalGradient(
                colors = listOf(Color.Yellow, Color(0xFFFFA500)),
            )

        levels.forEachIndexed { i, anim ->
            val h = anim.value * size.height
            val x = i * (w * 1.5f)

            // glow behind
            drawRoundRect(
                color = Color.Yellow.copy(alpha = 0.1f),
                topLeft = Offset(x, size.height - h),
                size =
                    androidx.compose.ui.geometry
                        .Size(w, h),
                cornerRadius =
                    androidx.compose.ui.geometry
                        .CornerRadius(14f),
            )

            // main bar
            drawRoundRect(
                brush = gradient,
                topLeft = Offset(x, size.height - h),
                size =
                    androidx.compose.ui.geometry
                        .Size(w * 0.8f, h),
                cornerRadius =
                    androidx.compose.ui.geometry
                        .CornerRadius(20f),
            )
        }
    }
}

@Preview
@Composable
fun PreviewBatteryViz() {
    Box(Modifier.size(300.dp).background(Color.Black)) {
        BatteryViz()
    }
}
