# Windows Media Player Visualizer — Jetpack Compose 🎶

This project is a modern take on the classic Windows Media Player experience, rebuilt with **Jetpack Compose**.  
It includes multiple animated visualizers, a retro-inspired media player UI, and smooth fullscreen transitions.


![Windows Media Player Visualizer](demo.gif)


## ✨ Features

### 🎛 Media Player UI
- Classic WMP-style layout
- Track list with highlight for the current song
- Playback controls (play/pause, next, previous)
- Volume control
- Progress bar with animated playback time
- Toggleable playlist
- Theme colors inspired by Windows XP/Windows 7 WMP

### 🎨 Animated Visualizers
Each visualization runs independently and updates in real time using `withFrameNanos`.

#### 1. **Bars & Waves**
- Multi-channel bars
- Reactive sine waves
- Soft glow effects
- Random animation mode

#### 2. **Alchemy**
- Floating glowing “bubble” particles
- Rising motion
- Light bloom effect

#### 3. **Battery Pulse**
- Battery-style glowing blocks
- Height interpolation with smoothing (lerp)
- Vertical gradient tint

#### 4. **Musical Colors**
- Pulsing color gradients
- Dynamic background shifts
- Ambient motion

## 📱 Fullscreen Mode

Double-tap any visualizer to:
- Expand to fullscreen  
- Animate scale, opacity, and background  
- Hide UI chrome  
Double-tap again to return.

Smooth transitions handled by:
```kotlin
animateFloatAsState()
animateColorAsState()
animateDpAsState()
```



🧩 Tech Stack
- Jetpack Compose
- Material 3
- Compose Animations
- Edge-to-Edge layout
- Kotlin Coroutines
- pointerInput gesture detection


📦 Setup

Clone the repo:

git clone https://github.com/jacksonmafra-umain/Visualizations

Open it in Android Studio and run on a device or emulator.


🖼 Preview

The UI follows a square aspect ratio display area (like old WMP) and includes:
- Visualizer switcher button
- Playlist drawer
- Gradient top/bottom bars
- Glow, blur, and trailing motion effects


🧪 Roadmap
- Add audio-reactive mode (FFT)
- More visualizers (Particles, Orbs, Electric Lines)
- True WMP skin support
- Desktop Compose version
- Live wallpaper export


📜 License

MIT License. You can use or modify it freely.


Enjoy the nostalgia. 🎵🔥
