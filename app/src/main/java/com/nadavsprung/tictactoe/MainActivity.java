package com.nadavsprung.tictactoe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    // Game logic handler for regular tic-tac-toe (not currently used in Ultimate version)
    private Game game;

    // Tracks whose turn it is: 1 = O's turn, 2 = X's turn
    // USAGE: Set to 2 initially (X starts first)
    private int playerTurn=2;
    
    // Used to save/load user preferences (like username)
    private SharedPreferences sharedPreferences;
    
    // Stores the username that was saved
    private String savedUsername;
    
    // Array of 9 Game objects - one for each of the 9 smaller boards in Ultimate Tic-Tac-Toe
    // Each board has its own game logic
    Game[] arrgame = new Game[9];
    
    // Tracks which player won each board: 0 = not won, 1 = O won, 2 = X won
    int[] boardwinners = new int[9];
    
    // Tracks which board is currently active (where player must play next)
    // -1 = free choice (any board), 0-8 = specific board number
    // USAGE: Set to -1 initially (free choice mode at game start)
    int activeboard=-1;
    
    // Flag to prevent moves after game ends
    private boolean gameOver = false;


    /**
     * SUMMARY: Initializes the app when it starts - sets up UI, loads saved data, and prepares the game
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call parent class setup
        super.onCreate(savedInstanceState);
        // Make app go edge-to-edge (full screen)
        EdgeToEdge.enable(this);
        // Load the layout file
        setContentView(R.layout.activity_main);
        // Set up tags on all cells so we know which board/cell was clicked
        setupUltimateTicTacToe();
        // Create 9 Game objects (one for each board)
        setupgame();

        // Handle screen padding for system UI (like status bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Show starting message
        updateText("Starting game...");
        // Create game object (not really used in Ultimate version)
        game = new Game();
        // Get saved preferences
        this.sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        // Load saved username, or use "DefaultUser" if none saved
        this.savedUsername = sharedPreferences.getString("USERNAME", "DefaultUser");
        // Find username TextView and set it
        TextView t = findViewById(R.id.username);
        t.setText(savedUsername);
    }
    /**
     * SUMMARY: Creates 9 Game objects - one for each of the 9 smaller boards in Ultimate Tic-Tac-Toe
     */
    public void setupgame() {
        // Loop through all 9 boards
        for (int i = 0; i < 9; i++) {
            // Create a new Game object for this board
            arrgame[i] = new Game();
        }
    }

    /**
     * SUMMARY: Sets tags on all cells (like "3_5") so we can identify which board and cell was clicked
     */
    public void setupUltimateTicTacToe() {
        // Loop through all 9 boards
        for (int boardNum = 0; boardNum < 9; boardNum++) {
            // Get the ID of this board (like "board_0", "board_1", etc.)
            int boardId = getResources().getIdentifier("board_" + boardNum, "id", getPackageName());
            // Find the board layout
            LinearLayout board = findViewById(boardId);

            // Loop through all 9 cells in this board
            for (int cellNum = 0; cellNum < 9; cellNum++) {
                // Get the ID of this cell (like "imageView0", "imageView1", etc.)
                int cellId = getResources().getIdentifier("imageView" + cellNum, "id", getPackageName());
                // Find the cell ImageView inside this board
                ImageView cell = board.findViewById(cellId);
                // If cell exists, set a tag like "3_5" (board 3, cell 5)
                if (cell != null) {
                    cell.setTag(boardNum + "_" + cellNum);
                }
            }
        }
    }


    /**
     * SUMMARY: Handles when a player taps a cell - updates game logic, moves the red border, switches turns, and checks for wins
     */
    public void playerTap(View v) {
        // If game is over, don't allow any moves
        if (gameOver) return;
        
        // Get the tag from the clicked cell (like "3_5")
        String tag = v.getTag().toString();
        // Split it into parts: ["3", "5"]
        String[] parts = tag.split("_");
        // Get board number (0-8)
        int boardNum = Integer.parseInt(parts[0]);
        // Get cell number (0-8)
        int cellNum = Integer.parseInt(parts[1]);

        // If player clicked wrong board (and not free choice mode), ignore the click
        // USAGE: Check if activeboard != -1 (meaning we're NOT in free choice mode)
        if (boardNum != activeboard && activeboard != -1) return;

        // Update the game logic: mark this cell with current player
        arrgame[boardNum].setSpot(cellNum, playerTurn);

        // Remember which board was active before
        int oldActiveBoard = activeboard;
        // New active board = the cell number that was clicked
        activeboard = cellNum;
        // If the new active board is already won, allow free choice
        // USAGE: Check if boardwinners[activeboard] != 0 (board is already won)
        // USAGE: Set activeboard = -1 (switch to free choice mode)
        if (boardwinners[activeboard] != 0) {
            activeboard = -1;
        }

        // Check if this board was just won FIRST (before updating red borders)
        checkIfBoardWon(boardNum);
        
        // Remove red border from old active board (only if it's not won)
        if (oldActiveBoard >= 0 && oldActiveBoard <= 8 && boardwinners[oldActiveBoard] == 0) {
            LinearLayout oldBoard = findViewById(getResources().getIdentifier("board_" + oldActiveBoard, "id", getPackageName()));
            oldBoard.setBackgroundResource(R.drawable.board_inactive_boarder);
        }

        // Add red border to new active board (only if it's not won)
        if (activeboard >= 0 && activeboard <= 8 && boardwinners[activeboard] == 0) {
            LinearLayout newBoard = findViewById(getResources().getIdentifier("board_" + activeboard, "id", getPackageName()));
            newBoard.setBackgroundResource(R.drawable.board_active_boarder);
        }






        // If it was X's turn (playerTurn == 2)
        // USAGE: Check if playerTurn == 2 (X's turn)
        if (playerTurn == 2) {
            // Show X image on the cell
            ((ImageView) v).setImageResource(R.drawable.x);
            // Switch to O's turn
            // USAGE: Set playerTurn = 1 (switch to O's turn)
            playerTurn = 1;
            // Only update turn message if game is not over (win message takes priority)
            if (!gameOver) {
                // Update status text - show "Choose any spot" if in free choice mode
                if (activeboard == -1) {
                    updateText("Choose any spot");
                } else {
                    updateText("O's Turn...");
                }
                // Wait 1 second, then make computer's move
                new android.os.Handler().postDelayed(this::updateComputerUIMove, 1000);
            }
        } else {
            // It was O's turn, show O image
            ((ImageView) v).setImageResource(R.drawable.o);
            // Switch to X's turn
            // USAGE: Set playerTurn = 2 (switch to X's turn)
            playerTurn = 2;
            // Only update turn message if game is not over (win message takes priority)
            if (!gameOver) {
                // Update status text - show "Choose any spot" if in free choice mode
                if (activeboard == -1) {
                    updateText("Choose any spot");
                } else {
                    updateText("X's Turn...");
                }
            }
        }

        // Make this cell unclickable so it can't be clicked again
        ((ImageView) v).setClickable(false);

        // // Check for win condition after the move
        // if (game.didWin()) {
        //     MyDatabaseHelper db = new MyDatabaseHelper(MainActivity.this);

        //     if (playerTurn== 1) {
        //         updateText("X Won!!!");
        //         db.addLog("X", game.getMoves(), getDate());
        //     } else {
        //         updateText("O Won!!!");
        //         db.addLog("0", game.getMoves(), getDate());
        //     }
        //     gameOver = true;
        //     gameEnd(); // Show "Play Again" and lock board

        // }
    }


    /**
     * SUMMARY: Shows the "Play Again" button when the game ends
     */
    public void gameEnd() {
        Button b = findViewById(R.id.playAgain);
        b.setVisibility(View.VISIBLE);
    }

    /**
     * SUMMARY: Called when user presses "Play Again" button - resets the game and clears the game over flag
     */
    public void resetButton(View v) {
        // Reset everything
        resetGame();
        // Game is no longer over
        gameOver = false;
    }

    /**
     * SUMMARY: Clears all cells on the board and starts a fresh game
     */
    public void resetGame() {
        // Reset all 9 boards in Ultimate Tic-Tac-Toe
        for (int boardNum = 0; boardNum < 9; boardNum++) {
            // Get the board layout
            int boardId = getResources().getIdentifier("board_" + boardNum, "id", getPackageName());
            LinearLayout board = findViewById(boardId);
            
            // Reset board background to inactive border
            board.setBackgroundResource(R.drawable.board_inactive_boarder);
            
            // Loop through all 9 cells in this board
            for (int cellNum = 0; cellNum < 9; cellNum++) {
                // Get the cell ImageView
                int cellId = getResources().getIdentifier("imageView" + cellNum, "id", getPackageName());
                ImageView cell = board.findViewById(cellId);
                
                if (cell != null) {
                    // Clear the image
                    cell.setImageDrawable(null);
                    // Make it clickable again
                    cell.setClickable(true);
                }
            }
        }

        // Reset game state variables
        playerTurn = 2; // X starts first
        activeboard = -1; // Free choice mode at start
        gameOver = false; // Game is active
        
        // Reset board winners array
        for (int i = 0; i < 9; i++) {
            boardwinners[i] = 0;
        }
        
        // Recreate all 9 Game objects (fresh game logic for each board)
        setupgame();

        // Create new game object (for regular tic-tac-toe, though not used in Ultimate)
        game = new Game();
        // Show starting message
        updateText("Starting new game...");
    }

    /**
     * SUMMARY: Updates the status message text shown at the bottom of the screen
     */
    public void updateText(String message) {
        TextView t = findViewById(R.id.textViewStatus);
        t.setText(message);
    }

    /**
     * SUMMARY: Makes the computer's move - picks a board (or random if free choice), gets computer's cell choice, and simulates a tap
     */
    private void updateComputerUIMove() {
        // If game is over, don't make computer move
        if (gameOver) return;

        // Start from the active board
        int targetBoard = activeboard;

        // If active board is a specific board, make sure it's usable
        if (targetBoard >= 0 && targetBoard <= 8) {
            boolean hasEmpty = false;

            // If that board is already won, or has no empty cells, force free choice
            if (boardwinners[targetBoard] == 0) {
                int[] spots = arrgame[targetBoard].getSpot();
                for (int spot : spots) {
                    if (spot == 0) {
                        hasEmpty = true;
                        break;
                    }
                }
            }

            if (boardwinners[targetBoard] != 0 || !hasEmpty) {
                targetBoard = -1; // treat as free choice
            }
        }

        // If we're in free choice mode (or active board was unusable), pick any playable board
        if (targetBoard == -1) {
            int[] availableBoards = new int[9];
            int count = 0;
            for (int i = 0; i < 9; i++) {
                if (boardwinners[i] == 0) {
                    int[] spots = arrgame[i].getSpot();
                    boolean hasEmpty = false;
                    for (int spot : spots) {
                        if (spot == 0) {
                            hasEmpty = true;
                            break;
                        }
                    }
                    if (hasEmpty) {
                        availableBoards[count] = i;
                        count++;
                    }
                }
            }
            if (count == 0) {
                return; // No available boards
            }
            targetBoard = availableBoards[(int)(Math.random() * count)];
        }

        // Get computer's move (which cell to play)
        int x = arrgame[targetBoard].computerMove();
        if (x == -1) return; // Safety: board is full
        
        // Find the cell and make the move
        int boardId = getResources().getIdentifier("board_" + targetBoard, "id", getPackageName());
        LinearLayout board = findViewById(boardId);
        if (board == null) return;
        
        int cellId = getResources().getIdentifier("imageView" + x, "id", getPackageName());
        ImageView v = board.findViewById(cellId);
        
        if (v != null && v.isClickable()) {
            playerTap(v);
        }
    }


    /**
     * SUMMARY: Opens the win log/leaderboard screen to show past game results
     */
    public void goToLog(View v) {
        Intent i = new Intent(MainActivity.this, WinLogActivity.class);
        startActivity(i);
    }

    public void logout(View v) {
        FirebaseAuth.getInstance().signOut();
        getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).edit().clear().apply();
        startActivity(new Intent(MainActivity.this, OpenActivity.class));
        finish();
    }

    /**
     * SUMMARY: Returns the current date and time as a formatted string (e.g., "2024-01-15 14:30:45")
     */
    public String getDate() {
        // Format: "2024-01-15 14:30:45"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());
        return currentDateAndTime;
    }

    /**
     * SUMMARY: Checks if a specific board was won - if so, marks it with X/O background, disables all cells, and checks for ultimate win
     */
    public void checkIfBoardWon(int boardNum) {
        // If board already won, do nothing
        // USAGE: Check if boardwinners[boardNum] != 0 (board already won, skip)
        if (boardwinners[boardNum] != 0) return;

        // Check if this board has a winner
        if (arrgame[boardNum].didWin()) {
            

            // Find the board layout
            LinearLayout board = findViewById(getResources().getIdentifier("board_" + boardNum, "id", getPackageName()));
            // If it was O's turn when they won (playerTurn == 1 means O just played)
            // USAGE: Check if playerTurn == 1 (O just played and won)
            if (playerTurn == 1) {
                // Show O background on the whole board
                board.setBackgroundResource(R.drawable.o);
                // Mark that O won this board
                // USAGE: Set boardwinners[boardNum] = 1 (mark O won this board)
                boardwinners[boardNum] = 1;
            } else {
                // X won, show X background
                board.setBackgroundResource(R.drawable.x);
                // Mark that X won this board
                // USAGE: Set boardwinners[boardNum] = 2 (mark X won this board)
                boardwinners[boardNum] = 2;
            }
            // Disable all cells in this won board so no more moves can be made
            for (int cellNum = 0; cellNum < 9; cellNum++) {
                // Create tag like "3_5"
                String cellTag = boardNum + "_" + cellNum;
                // Find the cell using its tag
                ImageView cell = findViewById(android.R.id.content).findViewWithTag(cellTag);
                // Make it unclickable
                if (cell != null) {
                    cell.setClickable(false);
                }
            }

            // Check if someone won the whole Ultimate game
            checkUltimateWin();
        }
    }
    /**
     * SUMMARY: Checks if someone won the entire Ultimate Tic-Tac-Toe game (3 boards in a row/column/diagonal)
     * Uses the same pattern as Game.didWin() - loops through winning combinations
     */
    public void checkUltimateWin() {
        // If game already over, don't check again
        if (gameOver) return;
        
        // Define all winning combinations (same as Game class)
        int[][] winPositions = new int[][]{
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
            {0, 4, 8}, {2, 4, 6}             // Diagonals
        };
        
        // Loop through each winning combination (same pattern as Game.didWin())
        for (int i = 0; i < winPositions.length; i++) {
            int a = winPositions[i][0];
            int b = winPositions[i][1];
            int c = winPositions[i][2];
            
            // Get values from boardwinners array
            int boardA = boardwinners[a];
            int boardB = boardwinners[b];
            int boardC = boardwinners[c];
            
            // If all three boards are won by the same player (not 0), it's a win!
            // Check: boardA is not 0, and all three are equal
            if (boardA != 0 && boardA == boardB && boardB == boardC) {
                // Someone won! End the game
                gameOver = true;
                String winnerMessage;
                if (boardA == 1) {
                    // O won
                    winnerMessage = "O Won!";
                } else {
                    // X won
                    winnerMessage = "X Won!";
                }
                // Update status text (using the same TextView as turn messages)
                updateText(winnerMessage);
                // Show "Play Again" button
                gameEnd();
                
                // Disable all cells in all boards
                for (int boardNum = 0; boardNum < 9; boardNum++) {
                    for (int cellNum = 0; cellNum < 9; cellNum++) {
                        String cellTag = boardNum + "_" + cellNum;
                        ImageView cell = findViewById(android.R.id.content).findViewWithTag(cellTag);
                        if (cell != null) {
                            cell.setClickable(false);
                        }
                    }
                }
                return; // Exit early since we found a winner
            }
        }
        // No winner found yet
    }


}