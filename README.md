# Android Development Assignment

A collection of four core Android applications built to demonstrate fundamental concepts in mobile app development, including UI design, hardware sensor integration, media playback, and local file storage.

## Projects Included

### 1. Currency Converter & Theme Preferences
An interactive currency conversion app that calculates exchange rates. 
* **Key Concepts:** UI Layouts (`LinearLayout`, `ConstraintLayout`), Input Handling, and `SharedPreferences` to toggle and save a persistent Dark Mode theme.

### 2. Media Player App
A dual-purpose media player that handles both local files and network streams.
* **Key Concepts:** `MediaPlayer` API for local audio playback, `VideoView` for streaming `.mp4` URLs over the internet, Media Playback Lifecycle (Play, Pause, Stop, Restart), and Internet Permissions.

### 3. Hardware Sensor Reader
A utility app that monitors and displays real-time data from the device's physical hardware.
* **Key Concepts:** `SensorManager` API, `SensorEventListener`, managing application lifecycle (`onResume`, `onPause`) to prevent battery drain, and handling Accelerometer, Ambient Light, and Proximity data.

### 4. Camera & Gallery Grid
A custom camera application that takes photos, saves them to a private application directory, and displays them in an interactive grid.
* **Key Concepts:** `RecyclerView` and custom Adapters, Implicit Intents (`ACTION_IMAGE_CAPTURE`), `FileProvider` for secure URI sharing, Scoped Storage, Permissions handling, and `AlertDialog` for deleting files.

## Tech Stack
* **Language:** Java
* **UI/Layout:** XML
* **IDE:** Android Studio
* **Minimum SDK:** API 24 (Android 7.0)

## How to Run
1. Clone this repository to your local machine.
2. Open **Android Studio** and select `File > Open`.
3. Navigate to the specific project folder you wish to test.
4. Allow Gradle to sync the project.
5. Click the **Run** button to launch the app on an Android Emulator or a physical device connected via USB debugging.

---
*Developed by Riwan as part of B.Tech CSE coursework.*
