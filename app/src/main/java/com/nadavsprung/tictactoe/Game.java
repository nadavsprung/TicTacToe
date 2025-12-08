package com.nadavsprung.tictactoe;

public class Game {

    // 1 = O, 2 = X, 0 = empty

    private int[] spot; // Array representing the board (9 positions)
    private int[][] winPositions; // All winning combinations

    // Constructor: Initializes the game
    public Game() {
       // X starts the game

        // Initialize all spots to 0 (empty)
        this.spot = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};

        // Define all winning combinations (rows, columns, diagonals)
        this.winPositions = new int[][]{
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
                {0, 4, 8}, {2, 4, 6}             // Diagonals
        };
    }

    // Getter for player turn


    // Returns the current state of the board
    public int[] getSpot() {
        return spot;
    }

    // Marks a spot for the current player
    public void setSpot(int num,int turn) {
        this.spot[num] =turn;
    }

    // Checks if the current player has won
    public boolean didWin() {
        for (int i = 0; i < winPositions.length; i++) {
            int a = winPositions[i][0];
            int b = winPositions[i][1];
            int c = winPositions[i][2];

            // Get values from the board
            int spota = spot[a];
            int spotb = spot[b];
            int spotc = spot[c];

            // If all three spots are the same (not empty), it's a win
            if (spota != 0 && spota == spotb && spotb == spotc) {
                return true;
            }
        }
        return false; // No win found
    }

    // Picks a random empty spot for the computer
    public int computerMove() {
        int emptyCount = 0;

        // Count how many spots are empty
        for (int i = 0; i < spot.length; i++) {
            if (spot[i] == 0) {
                emptyCount++;
            }
        }

        // If the board is full, return -1 (no move possible)
        if (emptyCount == 0) {
            return -1;
        }

        // Pick a random empty spot index (not the index on board yet)
        int target = (int)(Math.random() * emptyCount);

        // Find the corresponding empty spot index on the board
        int count = 0;
        for (int i = 0; i < spot.length; i++) {
            if (spot[i] == 0) {
                if (count == target) {
                    return i;
                }
                count++;
            }
        }

        return -1; // Fallback, should never happen
    }
    public int getMoves(){
        int count=0;
        for (int i = 0; i < spot.length; i++) {
            if (0==spot[i]){
                count++;
            }

        }
        return 9-count;
    }


}
