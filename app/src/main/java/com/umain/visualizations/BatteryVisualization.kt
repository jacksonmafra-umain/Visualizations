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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlin.math.sin

data class EnergyBolt(
    var x: Float,
    var y: Float,
    val targetY: Float,
    val speed: Float,
    val hue: Float,
    val width: Float,
)

@Composable
fun BatteryVisualization(isPlaying: Boolean) {
    var time by remember { mutableStateOf(0f) }
    val bolts = remember { mutableStateListOf<EnergyBolt>() }
    var energyLevel by remember { mutableStateOf(0f) }

    LaunchedEffect(isPlaying) {
        while (true) {
            delay(16) // ~60 FPS
            time += 0.05f

            if (isPlaying) {
                energyLevel = kotlin.math.min(100f, energyLevel + Math.random().toFloat() * 3f)

                // Energy decay
                energyLevel = kotlin.math.max(0f, energyLevel - 0.5f)

                // Remove completed bolts
                bolts.removeAll { it.y <= it.targetY }
            } else {
                energyLevel = 0f
                bolts.clear()
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val centerX = width / 2
        val centerY = height / 2

        // Draw black background
        drawRect(
            color = Color.Black,
            topLeft = Offset.Zero,
            size = size,
        )

        if (isPlaying) {
            // Create new bolts randomly
            if (Math.random() < 0.3) {
                bolts.add(
                    EnergyBolt(
                        x = (Math.random() * width).toFloat(),
                        y = height,
                        targetY = (Math.random() * height * 0.3f).toFloat(),
                        speed = (Math.random() * 5 + 3).toFloat(),
                        hue = (Math.random() * 60 + 180).toFloat(),
                        width = (Math.random() * 3 + 2).toFloat(),
                    ),
                )
            }

            // Update and draw bolts
            bolts.forEach { bolt ->
                bolt.y -= bolt.speed

                // Draw lightning bolt
                val gradient =
                    Brush.verticalGradient(
                        colors =
                            listOf(
                                hslToColor(bolt.hue, 1f, 0.7f, 1f),
                                hslToColor(bolt.hue, 1f, 0.5f, 0f),
                            ),
                        startY = bolt.y,
                        endY = bolt.y + 50f,
                    )

                val path = Path()
                path.moveTo(bolt.x, bolt.y)

                // Create zigzag pattern
                repeat(5) { i ->
                    val segmentY = bolt.y + (i * 10)
                    val offsetX = (Math.random() - 0.5).toFloat() * 10
                    path.lineTo(bolt.x + offsetX, segmentY)
                }

                drawPath(
                    path = path,
                    color = hslToColor(bolt.hue, 1f, 0.7f),
                    style = Stroke(width = bolt.width),
                )
            }

            // Draw battery indicator bars
            val barCount = 10
            val barHeight = height / barCount
            val filledBars = ((energyLevel / 100f) * barCount).toInt()

            for (i in 0 until barCount) {
                val y = height - (i + 1) * barHeight
                val isFilled = i < filledBars

                if (isFilled) {
                    val hue = (i.toFloat() / barCount) * 120f + 180f
                    val pulse = sin(time * 3f - i * 0.3f) * 0.2f + 0.8f

                    drawRect(
                        color = hslToColor(hue, 1f, 0.5f * pulse, 0.6f),
                        topLeft = Offset(10f, y + 5f),
                        size =
                            androidx.compose.ui.geometry
                                .Size(30f, barHeight - 10f),
                    )
                } else {
                    drawRect(
                        color = Color.Transparent,
                        topLeft = Offset(10f, y + 5f),
                        size =
                            androidx.compose.ui.geometry
                                .Size(30f, barHeight - 10f),
                        style = Stroke(width = 2f),
                    )
                    drawRect(
                        color = Color(0xFF1e3a8a),
                        topLeft = Offset(10f, y + 5f),
                        size =
                            androidx.compose.ui.geometry
                                .Size(30f, barHeight - 10f),
                        style = Stroke(width = 2f),
                    )
                }
            }

            // Draw energy core
            val coreSize = 50f + sin(time * 2f) * 20f
            val coreGradient =
                Brush.radialGradient(
                    colors =
                        listOf(
                            Color(0xFF60a5fa),
                            Color(0xFF3b82f6).copy(alpha = 0.5f),
                            Color.Transparent,
                        ),
                    center = Offset(centerX, centerY),
                    radius = coreSize,
                )

            drawCircle(
                brush = coreGradient,
                radius = coreSize,
                center = Offset(centerX, centerY),
            )

            // Draw rotating energy ring
            rotate(degrees = time * 50f, pivot = Offset(centerX, centerY)) {
                drawCircle(
                    color = Color(0xFF3b82f6),
                    radius = 70f,
                    center = Offset(centerX, centerY),
                    style = Stroke(width = 3f),
                )
            }
        } else {
            // Idle battery
            drawRect(
                color = Color(0xFF1e40af),
                topLeft = Offset(centerX - 40f, centerY - 60f),
                size =
                    androidx.compose.ui.geometry
                        .Size(80f, 120f),
                style = Stroke(width = 4f),
            )
            drawRect(
                color = Color(0xFF1e3a8a),
                topLeft = Offset(centerX - 20f, centerY - 70f),
                size =
                    androidx.compose.ui.geometry
                        .Size(40f, 10f),
            )
        }
    }
}

@Preview(
    name = "Battery - Playing Preview",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400,
    heightDp = 800,
)
@Composable
fun BatteryVisualizationPlayingPreview() {
    BatteryVisualization(isPlaying = true)
}

@Preview(
    name = "Battery - Stopped Preview",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400,
    heightDp = 800,
)
@Composable
fun BatteryVisualizationStoppedPreview() {
    BatteryVisualization(isPlaying = false)
}
