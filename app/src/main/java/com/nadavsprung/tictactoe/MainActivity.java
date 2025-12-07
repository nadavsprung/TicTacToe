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

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Game game; // Game logic handler
    private SharedPreferences sharedPreferences;
    private String savedUsername;
    Game[] arrgame = new Game[9];
    int[] boardwinners = new int[9];
    int activeboard=-1;
    private boolean gameOver = false; // Tracks if the game has ended


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setupUltimateTicTacToe();
        setupgame();

        // Handle screen padding for system UI (like status bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Initialize UI and game logic
        updateText("Starting game...");
        game = new Game();
        this.sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        this.savedUsername = sharedPreferences.getString("USERNAME", "DefaultUser");
        TextView t = findViewById(R.id.username);
        t.setText(savedUsername);


    }
//setting the tags for board/cell to help identify

    public void setupgame() {

        for (int i = 0; i < 9; i++) {
            arrgame[i] = new Game();  // Initialize each board
        }

    }

    public void setupUltimateTicTacToe() {
        for (int boardNum = 0; boardNum < 9; boardNum++) {
            int boardId = getResources().getIdentifier("board_" + boardNum, "id", getPackageName());
            LinearLayout board = findViewById(boardId);

            for (int cellNum = 0; cellNum < 9; cellNum++) {
                int cellId = getResources().getIdentifier("imageView" + cellNum, "id", getPackageName());
                ImageView cell = board.findViewById(cellId);
                if (cell != null) {
                    cell.setTag(boardNum + "_" + cellNum);  // e.g., "3_5" = board 3, cell 5
                }
            }
        }
    }


    // Called when a player taps a cell
    public void playerTap(View v) {
        if (gameOver)
            return;

        String tag = v.getTag().toString();
        String[] parts = tag.split("_");
        int boardNum = Integer.parseInt(parts[0]);  // 0-8
        int cellNum = Integer.parseInt(parts[1]);   // 0-8

        if (boardNum!=activeboard && activeboard!=-1)return;

        arrgame[boardNum].setSpot(cellNum);// Update logic with player move

        // Store old active board before changing it
        int oldActiveBoard = activeboard;
        activeboard = cellNum;

        // Update board backgrounds - ADD THIS CODE
        if (oldActiveBoard >= 0 && oldActiveBoard <= 8) {
            LinearLayout oldBoard = findViewById(getResources().getIdentifier("board_" + oldActiveBoard, "id", getPackageName()));
            oldBoard.setBackgroundResource(R.drawable.board_inactive_boarder);
        }

        // Set new active board to active (red border)
        LinearLayout newBoard = findViewById(getResources().getIdentifier("board_" + activeboard, "id", getPackageName()));
        newBoard.setBackgroundResource(R.drawable.board_active_boarder);







        if (game.getPlayerTurn() == 2) {
            ((ImageView) v).setImageResource(R.drawable.x);
            game.setPlayerTurn(1);
            updateText("O's Turn...");

            // Delay computer's move by 1 second
            new android.os.Handler().postDelayed(this::updateComputerUIMove, 1000);
        } else {
            ((ImageView) v).setImageResource(R.drawable.o);
            game.setPlayerTurn(2);
            updateText("X's Turn...");
        }

        ((ImageView) v).setClickable(false); // Prevent re-tapping same cell

        // Check for win condition after the move
        if (game.didWin()) {
            MyDatabaseHelper db = new MyDatabaseHelper(MainActivity.this);

            if (game.getPlayerTurn() == 1) {
                updateText("X Won!!!");
                db.addLog("X", game.getMoves(), getDate());
            } else {
                updateText("O Won!!!");
                db.addLog("0", game.getMoves(), getDate());
            }
            gameOver = true;
            gameEnd(); // Show "Play Again" and lock board

        }
    }


    // Called at the end of a game to lock board and show reset button
    public void gameEnd() {
        Button b = findViewById(R.id.playAgain);
        b.setVisibility(View.VISIBLE);


    }

    // Called when user presses "Play Again"
    public void resetButton(View v) {
        resetGame();
        gameOver = false;
    }

    // Resets the UI and game logic for a new round
    public void resetGame() {
        for (int i = 0; i < 9; i++) {
            ImageView cell = findViewById(getResources().getIdentifier("imageView" + i, "id", getPackageName()));
            cell.setImageDrawable(null);
            cell.setClickable(true);
        }

        // Hide "Play Again" button
        Button b = findViewById(R.id.playAgain);
        b.setVisibility(View.GONE);

        // Restart the game logic
        game = new Game();
        updateText("Starting new game...");
    }

    // Updates the status message on screen
    public void updateText(String message) {
        TextView t = findViewById(R.id.textViewStatus);
        t.setText(message);
    }

    // Handles the computer's move on the board
    private void updateComputerUIMove() {

        int x = arrgame[activeboard].computerMove();
        ImageView v = null;
        // Find the board layout
        int boardId = getResources().getIdentifier("board_" + activeboard, "id", getPackageName());
        LinearLayout board = findViewById(boardId);

        // Then find the cell within that board
        int cellId = getResources().getIdentifier("imageView" + x, "id", getPackageName());
        v = board.findViewById(cellId);

        if (v != null) {
            playerTap(v); // Simulate the tap for computer
        }
    }


    public void goToLog(View v) {
        Intent i = new Intent(MainActivity.this, WinLogActivity.class);
        startActivity(i);

    }

    public String getDate() {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());

        return currentDateAndTime;

    }


}