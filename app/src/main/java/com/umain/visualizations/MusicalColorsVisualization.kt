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
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin
import kotlin.math.sqrt

data class ColorBlob(
    var x: Float,
    var y: Float,
    var radius: Float,
    var hue: Float,
    val saturation: Float,
    val lightness: Float,
    var vx: Float,
    var vy: Float,
    val pulsePhase: Float,
)

@Composable
fun MusicalColorsVisualization(isPlaying: Boolean) {
    var time by remember { mutableStateOf(0f) }
    val blobs = remember { mutableStateListOf<ColorBlob>() }

    // Initialize blobs
    LaunchedEffect(Unit) {
        repeat(8) {
            blobs.add(
                ColorBlob(
                    x = 0f,
                    y = 0f,
                    radius = (Math.random() * 60 + 40).toFloat(),
                    hue = (Math.random() * 360).toFloat(),
                    saturation = (Math.random() * 30 + 70).toFloat(),
                    lightness = (Math.random() * 20 + 50).toFloat(),
                    vx = ((Math.random() - 0.5) * 2).toFloat(),
                    vy = ((Math.random() - 0.5) * 2).toFloat(),
                    pulsePhase = (Math.random() * PI * 2).toFloat(),
                ),
            )
        }
    }

    LaunchedEffect(isPlaying) {
        while (true) {
            delay(16) // ~60 FPS
            time += 0.03f
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Draw black background
        drawRect(
            color = Color.Black,
            topLeft = Offset.Zero,
            size = size,
        )

        // Initialize blob positions if needed
        blobs.forEach { blob ->
            if (blob.x == 0f && blob.y == 0f) {
                blob.x = (Math.random() * width).toFloat()
                blob.y = (Math.random() * height).toFloat()
            }
        }

        if (isPlaying) {
            // Update and draw blobs
            blobs.forEachIndexed { index, blob ->
                // Update position
                blob.x += blob.vx
                blob.y += blob.vy

                // Bounce off edges
                if (blob.x < 0 || blob.x > width) blob.vx *= -1
                if (blob.y < 0 || blob.y > height) blob.vy *= -1

                // Keep within bounds
                blob.x = blob.x.coerceIn(0f, width)
                blob.y = blob.y.coerceIn(0f, height)

                // Update color
                blob.hue = (blob.hue + 0.5f) % 360f

                // Pulse effect
                val pulse = sin(time * 2f + blob.pulsePhase) * 0.3f + 1f
                val currentRadius = blob.radius * pulse

                // Draw blob with gradient
                val gradient =
                    Brush.radialGradient(
                        colors =
                            listOf(
                                hslToColor(blob.hue, blob.saturation / 100f, blob.lightness / 100f, 0.8f),
                                hslToColor(blob.hue, blob.saturation / 100f, blob.lightness / 100f, 0.4f),
                                hslToColor(blob.hue, blob.saturation / 100f, blob.lightness / 100f, 0f),
                            ),
                        center = Offset(blob.x, blob.y),
                        radius = currentRadius,
                    )

                drawCircle(
                    brush = gradient,
                    radius = currentRadius,
                    center = Offset(blob.x, blob.y),
                )

                // Add velocity variation
                val audioInfluence = sin(time * 3f + index).toFloat() * 0.1f
                blob.vx += audioInfluence
                blob.vy += audioInfluence

                // Damping
                blob.vx *= 0.98f
                blob.vy *= 0.98f
            }

            // Draw connecting lines between nearby blobs
            for (i in blobs.indices) {
                for (j in i + 1 until blobs.size) {
                    val blob1 = blobs[i]
                    val blob2 = blobs[j]
                    val dx = blob2.x - blob1.x
                    val dy = blob2.y - blob1.y
                    val distance = sqrt(dx * dx + dy * dy)

                    if (distance < 200f) {
                        val alpha = 1f - (distance / 200f)
                        drawLine(
                            color = Color.White.copy(alpha = alpha * 0.1f),
                            start = Offset(blob1.x, blob1.y),
                            end = Offset(blob2.x, blob2.y),
                            strokeWidth = 1f,
                        )
                    }
                }
            }

            // Draw central equalizer visualization
            val barCount = 20
            val barWidth = width / barCount

            for (i in 0 until barCount) {
                val barHeight = abs(sin(time * 2f + i * 0.5f)) * 100f + 20f
                val hue = (i.toFloat() / barCount) * 360f

                drawRect(
                    color = hslToColor(hue, 0.8f, 0.6f, 0.3f),
                    topLeft = Offset(i * barWidth, height / 2 - barHeight / 2),
                    size =
                        androidx.compose.ui.geometry
                            .Size(barWidth - 2f, barHeight),
                )
            }
        } else {
            // Idle state - reset velocities
            blobs.forEach { blob ->
                blob.vx = 0f
                blob.vy = 0f
            }

            // Draw static gradient circle
            val gradient =
                Brush.radialGradient(
                    colors =
                        listOf(
                            Color(0xFF3b82f6),
                            Color(0xFF8b5cf6),
                            Color.Black,
                        ),
                    center = Offset(width / 2, height / 2),
                    radius = 100f,
                )

            drawCircle(
                brush = gradient,
                radius = 100f,
                center = Offset(width / 2, height / 2),
            )
        }
    }
}
