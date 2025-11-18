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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun BarsViz(barCount: Int = 32) {
    val heights =
        remember {
            List(barCount) { Animatable(0f) }
        }

    // animate forever
    LaunchedEffect(Unit) {
        while (true) {
            heights.forEach { anim ->
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

    Canvas(
        modifier = Modifier.fillMaxSize(),
    ) {
        val w = size.width
        val h = size.height
        val barWidth = w / (barCount * 1.5f)
        val gap = barWidth / 2f

        // gradient
        val brush =
            Brush.verticalGradient(
                colors = listOf(Color.Cyan, Color.Blue),
            )

        heights.forEachIndexed { i, value ->
            val x = i * (barWidth + gap)
            val barHeight = value.value * h

            // top bars
            drawRoundRect(
                brush = brush,
                topLeft = Offset(x, h / 2 - barHeight / 2),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(6f, 6f),
                alpha = 0.9f,
            )

            // mirrored bars (bottom)
            drawRoundRect(
                brush = brush,
                topLeft = Offset(x, h / 2 + barHeight / 2),
                size = Size(barWidth, barHeight * 0.7f),
                cornerRadius = CornerRadius(6f, 6f),
                alpha = 0.3f,
            )

            // fake glow
            drawRoundRect(
                color = Color.Cyan.copy(alpha = 0.15f),
                topLeft = Offset(x - 4, h / 2 - barHeight / 2 - 4),
                size = Size(barWidth + 8, barHeight + 8),
                cornerRadius = CornerRadius(12f, 12f),
            )
        }
    }
}

@Preview
@Composable
fun PreviewBarsViz() {
    Box(Modifier.size(300.dp).background(Color.Black)) {
        BarsViz()
    }
}
