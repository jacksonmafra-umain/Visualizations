package com.umain.visualizations

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class Track(
    val id: Int,
    val title: String,
    val artist: String,
    val duration: String,
)

@Composable
fun MediaPlayerScreen() {
    val tracks =
        remember {
            listOf(
                Track(1, "Digital Dreams", "Synthwave Artist", "3:45"),
                Track(2, "Neon Nights", "Electronic Band", "4:12"),
                Track(3, "Cyber Paradise", "Future Sound", "3:28"),
                Track(4, "Virtual Reality", "Tech Beats", "5:03"),
                Track(5, "Electric Soul", "Digital Wave", "3:52"),
            )
        }

    var isPlaying by remember { mutableStateOf(false) }
    var currentTrack by remember { mutableStateOf(0) }
    var volume by remember { mutableStateOf(75f) }
    var currentTime by remember { mutableStateOf(0) }
    var showPlaylist by remember { mutableStateOf(false) }
    var currentViz by remember { mutableStateOf(0) }

    val visualizations = listOf("Bars and Waves", "Alchemy", "Battery", "Musical Colors")

    var isFullscreen by remember { mutableStateOf(false) }

    val heightAnim by animateFloatAsState(
        targetValue = if (isFullscreen) 1f else 0.5f,
        animationSpec = tween(400),
    )

    val scaleAnim by animateFloatAsState(
        targetValue = if (isFullscreen) 1.05f else 1f,
        animationSpec = tween(400),
    )

    val bgFade by animateColorAsState(
        targetValue = if (isFullscreen) Color.Black else Color(0xFF111111),
        animationSpec = tween(400),
    )

    LaunchedEffect(isPlaying, currentTrack) {
        if (isPlaying) {
            while (true) {
                delay(1000)
                val duration = parseTime(tracks[currentTrack].duration)
                if (currentTime >= duration) {
                    currentTrack = (currentTrack + 1) % tracks.size
                    currentTime = 0
                } else {
                    currentTime++
                }
            }
        }
    }

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = Color(0xFF1e293b),
            ),
    ) {
        Column {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(
                            brush =
                                Brush.horizontalGradient(
                                    colors =
                                        listOf(
                                            Color(0xFF2563eb),
                                            Color(0xFF1e40af),
                                        ),
                                ),
                        ).padding(16.dp),
            ) {
                if (!isFullscreen) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier =
                                    Modifier
                                        .size(24.dp)
                                        .background(Color(0xFF60a5fa), RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MusicNote,
                                    contentDescription = null,
                                    tint = Color(0xFF1e40af),
                                    modifier = Modifier.size(16.dp),
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Windows Media Player",
                                color = Color.White,
                                fontSize = 14.sp,
                            )
                        }
                        IconButton(onClick = { showPlaylist = !showPlaylist }) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "Playlist",
                                tint = Color.White,
                            )
                        }
                    }
                }
            }

            if (showPlaylist) {
                PlaylistView(
                    tracks = tracks,
                    currentTrack = currentTrack,
                    onTrackSelect = { index ->
                        currentTrack = index
                        currentTime = 0
                        showPlaylist = false
                        isPlaying = true
                    },
                )
            } else {
                Column {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(heightAnim)
                                .graphicsLayer {
                                    scaleX = scaleAnim
                                    scaleY = scaleAnim
                                }.background(bgFade)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onDoubleTap = {
                                            isFullscreen = !isFullscreen
                                        },
                                    )
                                },
                    ) {
                        when (currentViz) {
                            0 -> BarsAndWavesVisualization(isPlaying = isPlaying)
                            1 -> AlchemyVisualization(isPlaying = isPlaying)
                            2 -> BatteryVisualization(isPlaying = isPlaying)
                            3 -> MusicalColorsVisualization(isPlaying = isPlaying)
                        }

                        if (!isFullscreen) {
                            Button(
                                onClick = { currentViz = (currentViz + 1) % visualizations.size },
                                modifier =
                                    Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp),
                                colors =
                                    ButtonDefaults.buttonColors(
                                        containerColor = Color.Black.copy(alpha = 0.5f),
                                    ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            ) {
                                Text(
                                    text = visualizations[currentViz],
                                    fontSize = 10.sp,
                                    color = Color.White,
                                )
                            }
                        }
                    }

                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .background(
                                    brush =
                                        Brush.verticalGradient(
                                            colors =
                                                listOf(
                                                    Color(0xFF334155),
                                                    Color(0xFF1e293b),
                                                ),
                                        ),
                                ).padding(16.dp),
                    ) {
                        Column {
                            Text(
                                text = tracks[currentTrack].title,
                                color = Color(0xFF93c5fd),
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = tracks[currentTrack].artist,
                                color = Color(0xFF60a5fa).copy(alpha = 0.7f),
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }

                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF1e293b))
                                .padding(16.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = formatTime(currentTime),
                                color = Color(0xFF93c5fd),
                                fontSize = 12.sp,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier =
                                    Modifier
                                        .weight(1f)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF0f172a)),
                            ) {
                                val progress = currentTime.toFloat() / parseTime(tracks[currentTrack].duration)
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(progress)
                                            .background(
                                                brush =
                                                    Brush.horizontalGradient(
                                                        colors =
                                                            listOf(
                                                                Color(0xFF3b82f6),
                                                                Color(0xFF60a5fa),
                                                            ),
                                                    ),
                                            ),
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tracks[currentTrack].duration,
                                color = Color(0xFF93c5fd),
                                fontSize = 12.sp,
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(
                                onClick = {
                                    currentTrack = (currentTrack - 1 + tracks.size) % tracks.size
                                    currentTime = 0
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipPrevious,
                                    contentDescription = "Previous",
                                    tint = Color(0xFF93c5fd),
                                    modifier = Modifier.size(32.dp),
                                )
                            }

                            Spacer(modifier = Modifier.width(24.dp))

                            FloatingActionButton(
                                onClick = { isPlaying = !isPlaying },
                                modifier = Modifier.size(56.dp),
                                containerColor = Color(0xFF3b82f6),
                                shape = CircleShape,
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Pause" else "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp),
                                )
                            }

                            Spacer(modifier = Modifier.width(24.dp))

                            IconButton(
                                onClick = {
                                    currentTrack = (currentTrack + 1) % tracks.size
                                    currentTime = 0
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = "Next",
                                    tint = Color(0xFF93c5fd),
                                    modifier = Modifier.size(32.dp),
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Volume",
                                tint = Color(0xFF60a5fa),
                                modifier = Modifier.size(20.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Slider(
                                value = volume,
                                onValueChange = { volume = it },
                                valueRange = 0f..100f,
                                modifier = Modifier.weight(1f),
                                colors =
                                    SliderDefaults.colors(
                                        thumbColor = Color(0xFF60a5fa),
                                        activeTrackColor = Color(0xFF60a5fa),
                                        inactiveTrackColor = Color(0xFF0f172a),
                                    ),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = volume.toInt().toString(),
                                color = Color(0xFF60a5fa),
                                fontSize = 12.sp,
                                modifier = Modifier.width(32.dp),
                            )
                        }
                    }
                }
            }

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(
                            brush =
                                Brush.horizontalGradient(
                                    colors =
                                        listOf(
                                            Color(0xFF1e3a8a),
                                            Color(0xFF172554),
                                        ),
                                ),
                        ).padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "${if (isPlaying) "▶ Playing" else "⏸ Paused"} • Track ${currentTrack + 1} of ${tracks.size}",
                    color = Color(0xFF60a5fa).copy(alpha = 0.7f),
                    fontSize = 12.sp,
                )
            }
        }
    }
}

@Preview(
    name = "Media Player Preview",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400,
    heightDp = 800,
)
@Composable
fun MediaPlayerScreenPreview() {
    MediaPlayerScreen()
}

fun parseTime(timeStr: String): Int {
    val parts = timeStr.split(":")
    return parts[0].toInt() * 60 + parts[1].toInt()
}

fun formatTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return "$min:${sec.toString().padStart(2, '0')}"
}
