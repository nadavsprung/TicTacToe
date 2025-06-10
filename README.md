# TicTacToe Android App
![App Demo](tic-min-min.gif)

A classic TicTacToe game built for Android devices with player vs computer gameplay, win tracking, and game history features.

## 📱 Features

- **Player vs Computer**: Play against an AI opponent
- **Interactive UI**: Tap-to-play interface with visual feedback
- **Win Detection**: Automatic detection of winning combinations
- **Game History**: Track and view past game results with timestamps
- **Play Again**: Quick reset functionality for continuous gameplay
- **Move Counter**: Tracks the number of moves per game
- **Modern Android UI**: Built with Material Design components

## 🎮 How to Play

1. Launch the app to start a new game
2. You play as **X**, the computer plays as **O**
3. Tap any empty cell to make your move
4. The computer will automatically make its move after a 1-second delay
5. First player to get three in a row (horizontally, vertically, or diagonally) wins!
6. View your game history by tapping the log button
7. Tap "Play Again" to start a new round

## 🔧 Technical Details

### Requirements
- **Android API Level**: 27+ (Android 8.1 Oreo)
- **Target SDK**: 35 (Android 15)
- **Java Version**: 11
- **Gradle**: Kotlin DSL


### Architecture
The app follows a simple MVC pattern:
- **MainActivity.java**: Main game controller and UI logic
- **Game.java**: Core game logic and win detection
- **WinLogActivity.java**: Game history display
- **MyDatabaseHelper.java**: SQLite database for storing game logs
- **RecyclerViewCustomAdapter.java**: Adapter for displaying game history
- **Log.java**: Data model for game log entries


### Building the Project

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/TicTacToe.git
   cd TicTacToe
   ```

2. **Open in Android Studio**:
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory and select it

3. **Sync the project**:
   - Android Studio will automatically prompt to sync Gradle files
   - Click "Sync Now" if prompted

4. **Build and Run**:
   - Connect an Android device or start an emulator
   - Click the "Run" button (green triangle) or press `Shift + F10`
   - Select your target device and click "OK"


## 📱 App Structure

```
app/src/main/
├── java/com/nadavsprung/tictactoe/
│   ├── MainActivity.java          # Main game activity
│   ├── Game.java                  # Game logic
│   ├── WinLogActivity.java        # Game history activity
│   ├── MyDatabaseHelper.java      # Database operations
│   ├── RecyclerViewCustomAdapter.java  # History list adapter
│   └── Log.java                   # Log data model
├── res/
│   ├── layout/                    # UI layouts
│   ├── drawable/                  # Game assets (X and O images)
│   ├── values/                    # Strings, colors, themes
│   └── mipmap/                    # App icons
└── AndroidManifest.xml
```


## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 👨‍💻 Developer

Created by Nadav Sprung

---

**Enjoy playing TicTacToe! 🎮** 