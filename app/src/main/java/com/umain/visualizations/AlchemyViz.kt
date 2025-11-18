package com.umain.visualizations

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

data class Bubble(
    var x: Float,
    var y: Float,
    var r: Float,
    var speed: Float,
)

@Composable
fun AlchemyViz() {
    val bubbles =
        remember {
            mutableStateListOf<Bubble>().apply {
                repeat(40) {
                    add(
                        Bubble(
                            x = Random.nextFloat(),
                            y = Random.nextFloat(),
                            r = Random.nextFloat() * 20f + 10f,
                            speed = Random.nextFloat() * 0.002f + 0.001f,
                        ),
                    )
                }
            }
        }

    // trigger redraw
    val tick = remember { androidx.compose.runtime.mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            bubbles.forEach {
                it.y -= it.speed
                if (it.y < 0f) it.y = 1f
            }
            tick.value++ // update state to force recomposition
            withFrameNanos {}
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        // read tick so Compose knows to redraw
        tick.value

        bubbles.forEach {
            val cx = it.x * size.width
            val cy = it.y * size.height

            // glow
            drawCircle(
                color = Color.Magenta.copy(alpha = 0.1f),
                radius = it.r * 2f,
                center = Offset(cx, cy),
            )

            drawCircle(
                color = Color.Magenta,
                radius = it.r,
                center = Offset(cx, cy),
            )
        }
    }
}

@Preview
@Composable
fun PreviewAlchemyViz() {
    Box(Modifier.size(300.dp).background(Color.Black)) {
        AlchemyViz()
    }
}
