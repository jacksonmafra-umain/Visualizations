package com.umain.visualizations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umain.visualizations.ui.theme.VisualizationsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VisualizationsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VisualizerDemo(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

enum class VizMode {
    BARS,
    WAVES,
    BATTERY,
    ALCHEMY,
}

@Composable
fun VisualizerDemo(modifier: Modifier = Modifier) {
    var mode by remember { mutableStateOf(VizMode.BARS) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            VizMode.values().forEach { item ->
                Button(
                    onClick = { mode = item },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(item.name)
                }
            }
        }
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color(0xFF111111)),
        ) {
            FakeVisualizer(mode)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewVisualizerDemo() {
    VisualizerDemo()
}

@Composable
fun FakeVisualizer(mode: VizMode) {
    when (mode) {
        VizMode.BARS -> BarsViz()
        VizMode.WAVES -> WavesViz()
        VizMode.BATTERY -> BatteryViz()
        VizMode.ALCHEMY -> AlchemyViz()
    }
}
