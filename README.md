# Tracking App

A simple study-time tracker built with Kotlin, Room, ViewModel/LiveData, Coroutines, and Material 3.

## Build & Run
- Android Studio Ladybug / AGP 8.x  
- Min SDK: 24  
- Open the project root and run the app configuration.

## Tests
- Unit tests: ./gradlew testDebugUnitTest  
- Instrumented tests: ./gradlew connectedDebugAndroidTest

## Features
- Set weekly goal
- Log study sessions (with optional note)
- View / edit / delete entries
- Progress summary with percent and goal exceed
- Room database with backup rules
- Espresso UI tests

## Folder Structure
- app/src/main/java/com/example/trackingapp/...
- data (Room entities/DAO/DB), repo (Repository, ServiceLocator), ui (Activities/ViewModels)
- androidTest (Espresso), test (unit tests)
