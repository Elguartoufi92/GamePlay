package com.nosjeux.xoudouqi.bo;
// Classe Player
public class Player {
    private int id;
    private String username;
    private String password;
    private int wins;
    private int losses;
    private int draws;

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
    }

    public Player(int id, String username, String password, int wins, int losses, int draws) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public void incrementWins() {
        this.wins++;
    }

    public void incrementLosses() {
        this.losses++;
    }

    public void incrementDraws() {
        this.draws++;
    }

    public int getTotalGames() {
        return wins + losses + draws;
    }

    public double getWinRate() {
        int total = getTotalGames();
        return total > 0 ? (double) wins / total * 100 : 0;
    }

    @Override
    public String toString() {
        return String.format("%s (V:%d D:%d N:%d - %.1f%%)",
                username, wins, losses, draws, getWinRate());
    }
}




