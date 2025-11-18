package com.umain.visualizations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlaylistView(
    tracks: List<Track>,
    currentTrack: Int,
    onTrackSelect: (Int) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = 400.dp),
    ) {
        // Playlist Header
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
                                        Color(0xFF1e40af),
                                    ),
                            ),
                    ).padding(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = Color(0xFF93c5fd),
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Now Playing",
                    color = Color.White,
                    fontSize = 16.sp,
                )
            }
        }

        // Track List
        tracks.forEachIndexed { index, track ->
            val isSelected = index == currentTrack

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(
                            if (isSelected) {
                                Color(0xFF1e3a8a).copy(alpha = 0.3f)
                            } else {
                                Color.Transparent
                            },
                        ).clickable { onTrackSelect(index) }
                        .padding(16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isSelected) "▶ ${track.title}" else track.title,
                            color = if (isSelected) Color(0xFF93c5fd) else Color(0xFFcbd5e1),
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = track.artist,
                            color = Color(0xFF64748b),
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = track.duration,
                        color = Color(0xFF64748b),
                        fontSize = 12.sp,
                    )
                }
            }

            // Divider
            if (index < tracks.size - 1) {
                Divider(
                    color = Color(0xFF1e293b),
                    thickness = 1.dp,
                )
            }
        }
    }
}
