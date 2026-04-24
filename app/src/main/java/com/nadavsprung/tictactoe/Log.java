package com.nadavsprung.tictactoe;

public class Log {
    private long id;
    private String winner;
    private int boardsWon;
    private String date;

    public Log(long id, String winner, int boardsWon, String date) {
        this.id = id;
        this.winner = winner;
        this.boardsWon = boardsWon;
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

    public int getBoardsWon() {
        return boardsWon;
    }

    public void setBoardsWon(int boardsWon) {
        this.boardsWon = boardsWon;
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
                ", boardsWon=" + boardsWon +
                ", date='" + date + '\'' +
                '}';
    }
}
