# CineScope

## Table of Contents
- [Motivation and Background](#motivation-and-background)
- [Features](#features)
- [Screenshots](#screenshots)
- [Technical Implementation](#technical-implementation)
  - [Clean Architecture](#clean-architecture)
  - [Database](#database)
  - [Networking](#networking)
  - [UI](#ui)
  - [Dependency Injection](#dependency-injection)
  - [Platform-Specific Implementations](#platform-specific-implementations)
- [How to Run](#how-to-run)
  - [Prerequisites](#prerequisites)
  - [Setup](#setup)
  - [Android](#android)
  - [iOS](#ios)
- [Future Steps](#future-steps)
- [License](#license)

**CineScope** is a cross-platform mobile application (iOS & Android) built with Kotlin Multiplatform and Compose Multiplatform that allows users to discover, track, and rate movies and TV series. The app integrates with The Movie Database (TMDB) API to provide rich content information and personalized recommendations.

## Motivation and Background

After exploring Kotlin Multiplatform's potential for code sharing across platforms, I wanted to build a real-world application that demonstrates the power of this technology. Instead of maintaining separate codebases for iOS and Android, CineScope achieves **95%+ code sharing** while delivering a native experience on both platforms.

The problem I wanted to solve was simple: movie enthusiasts often use multiple apps to discover content, track what they want to watch, and remember what they've already rated. CineScope consolidates these features into a single, beautiful, cross-platform experience with offline support and personalized recommendations.

I focused on creating a clean architecture that's maintainable, testable, and scalable. The app demonstrates best practices in modern mobile development, including reactive programming with Kotlin Flow, local-first architecture with SQLDelight, and a fully shared UI layer with Compose Multiplatform.

## Features

* **Content Discovery**
  * Browse trending movies and TV series
  * Search across TMDB's vast database
  * View detailed information (plot, cast, ratings, release dates)
  * Explore similar content recommendations
  * Filter by genre and popularity

* **Personalized Tracking**
  * Create and manage your watchlist
  * Rate content with a 0-5 star rating system
  * Track your viewing history
  * Get recommendations based on your ratings
  * View personalized statistics and insights

* **User Profile**
  * Customize profile with picture upload (iOS & Android)
  * Change display name
  * Switch between Light, Dark, and System themes
  * Glassmorphic UI design with smooth transitions

* **Offline-First Architecture**
  * Browse cached content without internet
  * Sync data automatically when online
  * Local database for watchlist and ratings
  * Optimized image caching with Coil

## Screenshots

> Add screenshots here showing:
> - Home screen with trending movies
> - Movie details screen
> - Watchlist screen
> - Profile/Settings screen
> - Light and Dark mode comparisons

<!--
<p align="center">
  <img src="screenshots/home.png" alt="Home Screen" width="25%"/>
  <img src="screenshots/details.png" alt="Details Screen" width="25%"/>
  <img src="screenshots/watchlist.png" alt="Watchlist" width="25%"/>
</p>

<p align="center">
  <img src="screenshots/profile.png" alt="Profile" width="25%"/>
  <img src="screenshots/dark_mode.png" alt="Dark Mode" width="25%"/>
</p>
-->

## Technical Implementation

In this section, I describe the architectural decisions, tradeoffs, and interesting challenges encountered during development.

### Clean Architecture

CineScope follows **Clean Architecture** principles with clear separation of concerns across three layers:

```
┌─────────────────────────────────────────────────┐
│         Presentation Layer                      │
│  (Screens, ViewModels, UI Components)          │
│  • Compose UI with Material3                   │
│  • MVVM pattern                                │
│  • Voyager navigation                          │
└─────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────┐
│           Domain Layer                          │
│  (Business Logic, Use Cases, Models)           │
│  • Pure Kotlin (no platform dependencies)      │
│  • Repository interfaces                       │
│  • Domain models                               │
└─────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────┐
│            Data Layer                           │
│  (Repositories, Data Sources)                  │
│  • SQLDelight for local storage                │
│  • Ktor for networking                         │
│  • Repository implementations                  │
└─────────────────────────────────────────────────┘
```

**Key Design Decisions:**
- Unidirectional data flow using Kotlin `StateFlow`
- Repository pattern abstracts data sources
- Use case pattern encapsulates business logic
- Dependency inversion through interfaces

One of the most interesting challenges was implementing **database migrations** in a modular way. Instead of embedding migration logic directly in platform-specific `DatabaseDriverFactory` classes, I created a centralized `DatabaseMigrations` object that's applied at the dependency injection layer. This keeps the code clean and makes future schema changes easy to manage.

### Database

I used **SQLDelight 2.0.2** as it provides type-safe database access with great Kotlin Multiplatform support. The database schema includes:

- **UserProfile**: User settings, name, profile picture, theme preference
- **Watchlist**: Movies/series marked to watch later
- **Ratings**: User ratings with timestamps
- **Cached Content**: Movies and TV series for offline access

**Notable Implementation:**
- Automatic schema migrations with version control
- Reactive queries with Flow for real-time UI updates
- Transactions for batch operations
- Platform-specific drivers (AndroidSqliteDriver / NativeSqliteDriver)

Example migration approach:
```kotlin
object DatabaseMigrations {
    fun applyMigrations(driver: SqlDriver) {
        migrateToVersion2(driver) // Adds themePreference column
    }
}
```

### Networking

For networking, I used **Ktor 3.1.0** with the following features:
- `ContentNegotiation` with JSON serialization
- Custom interceptors for API key injection
- Error handling with sealed `Result` types
- Automatic retry logic for failed requests
- Response caching for improved performance

**TMDB API Integration:**
```kotlin
interface MovieRepository {
    suspend fun getTrending(page: Int): Result<List<Movie>>
    suspend fun searchMovies(query: String): Result<List<Movie>>
    suspend fun getMovieDetails(id: Int): Result<MovieDetails>
}
```

**Tradeoff:** Initially considered GraphQL for more flexible queries, but REST with Ktor proved simpler and sufficient for TMDB's API structure.

### UI

The entire UI is built with **Compose Multiplatform 1.9.3** using Material3 components. This means **100% shared UI code** between iOS and Android with zero platform-specific screens.

**Design Highlights:**
- **Glassmorphic Cards**: Custom `CineScopeCard` with blur effects and gradients
- **Smooth Animations**: Content transitions, theme switching, image loading
- **Adaptive Layouts**: Responsive design that works on tablets and phones
- **Theme System**: Full dark/light mode support with user preference persistence

**Custom Components:**
- `RatingStars`: Interactive 5-star rating widget
- `MovieCard`: Reusable card with poster, title, and metadata
- `ThemeDropdown`: Settings dropdown with smooth animations
- `ImagePickerLauncher`: Platform-specific image selection

One challenge was implementing **image upload** across platforms. iOS and Android have completely different APIs for photo access:
- **Android**: Uses `ActivityResultContracts` with runtime permissions
- **iOS**: Uses `UIImagePickerController` with Info.plist permissions

I abstracted this with an `expect/actual` pattern:
```kotlin
// Common
expect fun rememberImagePickerLauncher(
    onImageSelected: (String?) -> Unit
): ImagePickerLauncher

// Platform-specific implementations handle native APIs
```

### Dependency Injection

I used **Koin 4.0.0** for dependency injection, which provides:
- Platform-independent module definitions
- ViewModel injection with lifecycle awareness
- Easy testing with mock modules
- Minimal boilerplate

**Module Structure:**
```kotlin
val appModule = module {
    // Data layer
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
    single { CineScopeDatabase(get()) }

    // Repositories
    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }

    // ViewModels
    viewModelOf(::HomeViewModel)
    viewModelOf(::DetailsViewModel)
}
```

### Platform-Specific Implementations

While 95% of code is shared, certain features require platform-specific code using Kotlin's `expect/actual` mechanism:

**Database Drivers:**
```kotlin
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

// Android: AndroidSqliteDriver with Context
// iOS: NativeSqliteDriver with file path
```

**Image Picker:**
```kotlin
// Android: Permission requests + Intent-based picker
// iOS: UIImagePickerController with Photo Library access
```

**Time Handling:**
```kotlin
expect class TimeProvider {
    fun now(): Instant
}

// Platform-specific implementations for timestamps
```

## How to Run

### Prerequisites

- **JDK 17 or higher**
- **Android Studio Ladybug** or later (for Android development)
- **Xcode 15+** (for iOS development, macOS only)
- **TMDB API Key** (free, see setup below)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/pborkovic/CineScope.git
   cd CineScope
   ```

2. **Get Your TMDB API Key**
   - Create a free account at [The Movie Database](https://www.themoviedb.org/)
   - Go to [API Settings](https://www.themoviedb.org/settings/api)
   - Request an API key (choose "Developer" option)
   - Copy your API v3 key

3. **Configure API Key**

   **Option A: Using local.properties (Recommended)**

   Add this line to `local.properties` in the project root:
   ```properties
   tmdb.api.key=your_actual_api_key_here
   ```

   **Option B: Using Environment Variable**
   ```bash
   export TMDB_API_KEY=your_actual_api_key_here
   ```

4. **Build the project**
   ```bash
   ./gradlew build
   ```

   The `generateBuildConfig` task runs automatically and creates `BuildConfig.kt` with your API key.

### Android

**Using Android Studio:**
1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Select an Android device or emulator
4. Click Run ▶️

**Using Terminal:**
```bash
# Build debug APK
./gradlew :composeApp:assembleDebug

# Install on connected device
./gradlew :composeApp:installDebug

# Build and run
./gradlew :composeApp:installDebug && adb shell am start -n com.cinescope.cinescope/.MainActivity
```

### iOS

**Using Xcode:**
1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Select a simulator or physical device (iPhone 12 or later recommended)
3. Build and run (⌘R)

**Using Terminal:**
```bash
# Build iOS framework for simulator (Apple Silicon)
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64

# Build for physical device (requires code signing)
./gradlew :composeApp:linkDebugFrameworkIosArm64

# Then open Xcode to run the app
open iosApp/iosApp.xcodeproj
```

## License

Movie data and images are provided by [The Movie Database (TMDB)](https://www.themoviedb.org/). This product uses the TMDB API but is not endorsed or certified by TMDB.

---

**Learn more about:**
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [TMDB API Documentation](https://developers.themoviedb.org/3)
- [SQLDelight](https://cashapp.github.io/sqldelight/)
- [Ktor](https://ktor.io/)
- [Koin](https://insert-koin.io/)
