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
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var life: Float,
    val maxLife: Float,
    val hue: Float,
)

@Composable
fun AlchemyVisualization(isPlaying: Boolean) {
    var time by remember { mutableStateOf(0f) }
    val particles = remember { mutableStateListOf<Particle>() }

    LaunchedEffect(isPlaying) {
        while (true) {
            delay(16) // ~60 FPS
            time += 0.02f

            if (isPlaying) {
                // Remove dead particles
                particles.removeAll { it.life >= it.maxLife }
            } else {
                particles.clear()
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val centerX = width / 2
        val centerY = height / 2

        // Draw black background with fade
        drawRect(
            color = Color.Black.copy(alpha = 0.1f),
            topLeft = Offset.Zero,
            size = size,
        )

        if (isPlaying) {
            // Create new particles
            repeat(3) {
                val angle = Math.random() * PI * 2
                val speed = (Math.random() * 2 + 1).toFloat()

                particles.add(
                    Particle(
                        x = centerX,
                        y = centerY,
                        vx = (cos(angle) * speed).toFloat(),
                        vy = (sin(angle) * speed).toFloat(),
                        life = 0f,
                        maxLife = (Math.random() * 100 + 50).toFloat(),
                        hue = (Math.random() * 60 + 180).toFloat(),
                    ),
                )
            }

            // Update and draw particles
            particles.forEach { particle ->
                particle.x += particle.vx
                particle.y += particle.vy
                particle.life++

                // Apply turbulence
                particle.vx += (Math.random() - 0.5).toFloat() * 0.3f
                particle.vy += (Math.random() - 0.5).toFloat() * 0.3f

                val alpha = 1f - (particle.life / particle.maxLife)
                val particleSize = 3f + sin(time + particle.x * 0.01f) * 2f

                // Draw particle with glow
                drawCircle(
                    color = hslToColor(particle.hue, 1f, 0.6f, alpha),
                    radius = particleSize,
                    center = Offset(particle.x, particle.y),
                )
            }

            // Draw central orb
            val orbSize = 30f + sin(time * 2f) * 10f
            val gradient =
                Brush.radialGradient(
                    colors =
                        listOf(
                            Color(0xFF8b5cf6),
                            Color(0xFF3b82f6).copy(alpha = 0.6f),
                            Color.Transparent,
                        ),
                    center = Offset(centerX, centerY),
                    radius = orbSize,
                )

            drawCircle(
                brush = gradient,
                radius = orbSize,
                center = Offset(centerX, centerY),
            )
        } else {
            // Idle state - pulsing circle
            val size = 40f + sin(time * 2f) * 5f
            drawCircle(
                color = Color(0xFF3b82f6),
                radius = size,
                center = Offset(centerX, centerY),
            )
        }
    }
}

fun hslToColor(
    h: Float,
    s: Float,
    l: Float,
    alpha: Float = 1f,
): Color {
    val c = (1f - kotlin.math.abs(2 * l - 1)) * s
    val x = c * (1 - kotlin.math.abs((h / 60f) % 2 - 1))
    val m = l - c / 2

    val (r, g, b) =
        when {
            h < 60 -> Triple(c, x, 0f)
            h < 120 -> Triple(x, c, 0f)
            h < 180 -> Triple(0f, c, x)
            h < 240 -> Triple(0f, x, c)
            h < 300 -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }

    return Color(
        red = (r + m),
        green = (g + m),
        blue = (b + m),
        alpha = alpha,
    )
}

@Preview(
    name = "Alchemy - Playing Preview",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400,
    heightDp = 800,
)
@Composable
fun AlchemyVisualizationPlayingPreview() {
    AlchemyVisualization(isPlaying = true)
}

@Preview(
    name = "Alchemy - Stopped Preview",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400,
    heightDp = 800,
)
@Composable
fun AlchemyVisualizationStoppedPreview() {
    AlchemyVisualization(isPlaying = false)
}
