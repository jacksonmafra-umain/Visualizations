package com.umain.visualizations

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlin.math.sin

@Composable
fun BarsAndWavesVisualization(isPlaying: Boolean) {
    var time by remember { mutableStateOf(0f) }
    val barCount = 32
    val bars = remember { mutableStateListOf(*Array(barCount) { 0f }) }
    val wavePoints = remember { mutableStateListOf(*Array(50) { 0f }) }

    LaunchedEffect(isPlaying) {
        while (true) {
            delay(16) // ~60 FPS
            time += 0.05f

            if (isPlaying) {
                // Update bars
                for (i in 0 until barCount) {
                    bars[i] = (sin(time + i * 0.3f) * 0.5f + Math.random().toFloat() * 0.5f).toFloat()
                }

                // Update wave
                for (i in wavePoints.indices) {
                    wavePoints[i] = sin(time * 2f + i * 0.2f) * 0.3f
                }
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        if (isPlaying) {
            // Draw bars
            val barWidth = width / barCount
            bars.forEachIndexed { index, value ->
                val barHeight = value * height * 0.6f
                val x = index * barWidth
                val y = height - barHeight

                val gradient =
                    Brush.verticalGradient(
                        colors =
                            listOf(
                                Color(0xFF3b82f6),
                                Color(0xFF8b5cf6),
                                Color(0xFFec4899),
                            ),
                        startY = y,
                        endY = height,
                    )

                drawRect(
                    brush = gradient,
                    topLeft = Offset(x, y),
                    size =
                        androidx.compose.ui.geometry
                            .Size(barWidth - 2f, barHeight),
                )
            }

            // Draw first wave
            val wavePath1 = Path()
            val waveSpacing = width / wavePoints.size
            wavePoints.forEachIndexed { index, value ->
                val x = index * waveSpacing
                val y = height * 0.3f + value * 100f

                if (index == 0) {
                    wavePath1.moveTo(x, y)
                } else {
                    wavePath1.lineTo(x, y)
                }
            }

            drawPath(
                path = wavePath1,
                color = Color(0xFF60a5fa),
                style = Stroke(width = 3f),
            )

            // Draw second wave
            val wavePath2 = Path()
            wavePoints.forEachIndexed { index, value ->
                val x = index * waveSpacing
                val y = height * 0.5f + value * 80f

                if (index == 0) {
                    wavePath2.moveTo(x, y)
                } else {
                    wavePath2.lineTo(x, y)
                }
            }

            drawPath(
                path = wavePath2,
                color = Color(0xFFa78bfa),
                style = Stroke(width = 2f),
            )
        } else {
            // Idle state
            drawRect(
                color = Color(0xFF1e40af),
                topLeft = Offset(width / 2 - 50f, height / 2 - 50f),
                size =
                    androidx.compose.ui.geometry
                        .Size(100f, 100f),
            )
            drawRect(
                color = Color(0xFF3b82f6),
                topLeft = Offset(width / 2 - 40f, height / 2 - 40f),
                size =
                    androidx.compose.ui.geometry
                        .Size(80f, 80f),
            )
        }
    }
}


@Preview(
    name = "BarsAndWaves - Playing Preview",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400,
    heightDp = 800,
)
@Composable
fun BarsAndWavesVisualizationPlayingPreview() {
    BarsAndWavesVisualization(isPlaying = true)
}

@Preview(
    name = "BarsAndWaves - Stopped Preview",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400,
    heightDp = 800,
)
@Composable
fun BarsAndWavesVisualizationStoppedPreview() {
    BarsAndWavesVisualization(isPlaying = false)
}