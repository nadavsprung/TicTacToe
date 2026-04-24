# TicTacToe Android App
![App Demo](tic-min-min.gif)

A classic **Ultimate TicTacToe** game built for Android, featuring Firebase authentication, text-to-speech, move animations, and game history.

## Features

- **Ultimate Tic-Tac-Toe**: 9 boards in a 3×3 meta-grid — win 3 boards to win the game
- **Player vs Computer**: Play against an AI opponent
- **Firebase Authentication**: Sign up and log in with email/password
- **Text-to-Speech**: Game events are announced aloud
- **Placement Animations**: Smooth animations when pieces are placed
- **Active Board Highlighting**: Visual indicator shows which board you must play in next
- **Win Detection**: Per-board and meta-board win detection
- **Game History**: Track and view past game results with timestamps
- **Play Again**: Quick reset functionality for continuous gameplay

## How to Play

1. Launch the app and sign in or register
2. You play as **X**, the computer plays as **O**
3. Tap any empty cell in the active board to make your move
4. The cell you pick sends your opponent to the corresponding board (Ultimate rules)
5. If the target board is already won, you get a free choice of any open board
6. Win 3 boards in a row (horizontally, vertically, or diagonally) to win the match
7. View game history by tapping the log button
8. Tap "Play Again" to start a new round

## Technical Details

### Requirements
- **Android API Level**: 27+ (Android 8.1 Oreo)
- **Target SDK**: 35 (Android 15)
- **Java Version**: 11
- **Gradle**: Kotlin DSL

### Architecture

| File | Role |
|---|---|
| `OpenActivity.java` | Splash/auth entry point, hosts login/register fragments |
| `LoginFragment.java` | Firebase email/password login |
| `RegisterFragment.java` | Firebase account registration |
| `MainActivity.java` | Ultimate TicTacToe game controller and UI logic |
| `Game.java` | Core game logic and win detection (per-board) |
| `PlacementAnimator.java` | Piece placement animations |
| `WinLogActivity.java` | Game history display |
| `MyDatabaseHelper.java` / `DatabaseHelper.java` | SQLite database for storing game logs |
| `RecyclerViewCustomAdapter.java` | Adapter for displaying game history |
| `Log.java` | Data model for game log entries |

### Building the Project

1. **Clone the repository**:
   ```bash
   git clone https://github.com/nadavsprung/TicTacToe.git
   cd TicTacToe
   ```

2. **Add `google-services.json`**:
   - Create a Firebase project at [console.firebase.google.com](https://console.firebase.google.com)
   - Enable Email/Password authentication
   - Download `google-services.json` and place it in the `app/` directory

3. **Open in Android Studio**:
   - Open Android Studio → "Open an existing project" → select the cloned directory

4. **Sync and Run**:
   - Click "Sync Now" when prompted for Gradle sync
   - Connect a device or start an emulator
   - Click Run or press `Shift + F10`

## App Structure

```
app/src/main/
├── java/com/nadavsprung/tictactoe/
│   ├── OpenActivity.java              # Auth entry point
│   ├── LoginFragment.java             # Firebase login
│   ├── RegisterFragment.java          # Firebase registration
│   ├── MainActivity.java              # Ultimate TicTacToe game
│   ├── Game.java                      # Per-board game logic
│   ├── PlacementAnimator.java         # Piece animations
│   ├── WinLogActivity.java            # Game history activity
│   ├── MyDatabaseHelper.java          # SQLite helper
│   ├── DatabaseHelper.java            # SQLite helper (alternate)
│   ├── RecyclerViewCustomAdapter.java # History list adapter
│   └── Log.java                       # Log data model
├── res/
│   ├── layout/                        # UI layouts
│   ├── drawable/                      # X/O images, board borders
│   ├── anim/                          # Slide animations
│   ├── font/                          # Assistant font family
│   ├── values/                        # Strings, colors, themes
│   └── mipmap/                        # App icons
└── AndroidManifest.xml
```

## License

This project is open source and available under the [MIT License](LICENSE).

## Developer

Created by Nadav Sprung
