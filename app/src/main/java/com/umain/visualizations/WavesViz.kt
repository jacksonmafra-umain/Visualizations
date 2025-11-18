package com.umain.visualizations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun WavesViz() {
    val phase = remember { Animatable(0f) }

    // animate the phase forever
    LaunchedEffect(Unit) {
        while (true) {
            phase.animateTo(
                targetValue = phase.value + 2f,
                animationSpec =
                    tween(
                        durationMillis = 700,
                        easing = LinearEasing,
                    ),
            )
        }
    }

    Canvas(
        modifier = Modifier.fillMaxSize(),
    ) {
        val w = size.width
        val h = size.height
        val mid = h / 2f

        val waveAmp = h * 0.2f
        val freq = 1.5f

        val brush =
            Brush.horizontalGradient(
                listOf(Color.Magenta, Color.Cyan),
            )

        val path = Path()

        path.moveTo(0f, mid)

        for (x in 0 until w.toInt()) {
            val fx = x.toFloat()
            val y = (sin((fx / w) * freq * 2f * PI + phase.value) * waveAmp).toFloat()
            path.lineTo(fx, mid + y)
        }

        // bottom reflection
        val pathMirror = Path()
        pathMirror.moveTo(0f, mid)

        for (x in 0 until w.toInt()) {
            val fx = x.toFloat()
            val y = (sin((fx / w) * freq * 2f * PI + phase.value) * waveAmp * 0.6).toFloat()
            pathMirror.lineTo(fx, mid - y)
        }

        drawPath(path, brush, alpha = 0.8f)
        drawPath(pathMirror, brush, alpha = 0.2f)
    }
}

@Preview
@Composable
fun PreviewWavesViz() {
    Box(Modifier.size(300.dp).background(Color.Black)) {
        WavesViz()
    }
}
