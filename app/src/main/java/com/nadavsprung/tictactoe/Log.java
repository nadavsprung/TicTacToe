package com.nadavsprung.tictactoe;

public class Log {
    private long id;
    private String winner;
    private int moves;
    private String date;

    public Log(long id, String winner, int moves, String date) {
        this.id = id;
        this.winner = winner;
        this.moves = moves;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", winner='" + winner + '\'' +
                ", moves=" + moves +
                ", date='" + date + '\'' +
                '}';
    }
}
