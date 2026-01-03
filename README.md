# CineScope

A Kotlin Multiplatform movie tracking and recommendation app for Android and iOS, powered by The Movie Database (TMDB) API.

## Features

- Browse and search movies from TMDB
- Rate movies with a 0-5 star system
- Manage your watchlist
- Get personalized movie recommendations
- View your movie statistics
- Light and dark mode support

## Project Structure

This is a Kotlin Multiplatform project targeting Android and iOS.

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - [commonMain](./composeApp/src/commonMain/kotlin) is for code that's common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
      For example, if you want to use Apple's CoreCrypto for the iOS part of your Kotlin app,
      the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you're sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

## Setup Instructions

### Prerequisites

- JDK 11 or higher
- TMDB API Key (free)

### 1. Get Your TMDB API Key

1. Create a free account at [The Movie Database](https://www.themoviedb.org/)
2. Go to [API Settings](https://www.themoviedb.org/settings/api)
3. Request an API key (choose "Developer" option)
4. Copy your API key

### 2. Configure API Key

**Option A: Using local.properties (Recommended for local development)**

Add the following line to `local.properties` in the project root:

```properties
tmdb.api.key=your_actual_api_key_here
```

**Option B: Using Environment Variable (Recommended for CI/CD)**

```bash
export TMDB_API_KEY=your_actual_api_key_here
```

### 3. Generate BuildConfig

The project uses a Gradle task to generate `BuildConfig.kt` from your API key:

```bash
./gradlew generateBuildConfig
```

This task runs automatically before compilation, so you can also just build the project:

```bash
# Android
./gradlew :composeApp:assembleDebug

# iOS
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
```

## Building and Running

### Android

**Using Android Studio:**
1. Open the project in Android Studio
2. Select the Android run configuration
3. Run the app on an emulator or physical device

**Using Terminal:**
```bash
# Build debug APK
./gradlew :composeApp:assembleDebug

# Install on connected device
./gradlew :composeApp:installDebug
```

### iOS

**Using Xcode:**
1. Open the `/iosApp/iosApp.xcodeproj` in Xcode
2. Select a simulator or physical device
3. Build and run

**Using Terminal:**
```bash
# Build iOS framework
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64

# For physical device (M1+ Mac)
./gradlew :composeApp:linkDebugFrameworkIosArm64
```

## Tech Stack

- **Kotlin Multiplatform**: Shared business logic
- **Compose Multiplatform**: Shared UI across platforms
- **Ktor**: Networking (TMDB API integration)
- **SQLDelight**: Local database for offline support
- **Koin**: Dependency injection
- **Voyager**: Navigation
- **Coil**: Image loading
- **Material3**: Design system

## Architecture

The app follows **Clean Architecture** principles with three layers:

```
Presentation (UI + ViewModels)
    ↓
Domain (Use Cases + Business Logic)
    ↓
Data (Repositories + Local DB + Remote API)
```

### Key Features:
- MVVM pattern with unidirectional data flow
- Repository pattern for data abstraction
- Use case pattern for business logic encapsulation
- Offline-first with local caching
- Reactive state management with Kotlin Flow

## Security

⚠️ **Important**: Never commit `BuildConfig.kt` or `local.properties` with real API keys to version control!

- `BuildConfig.kt` is auto-generated and gitignored
- `local.properties` is gitignored by default
- Use environment variables for CI/CD pipelines

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is for educational purposes. Movie data is provided by [The Movie Database (TMDB)](https://www.themoviedb.org/).

---

Learn more about:
- [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/)
- [TMDB API](https://developers.themoviedb.org/3)