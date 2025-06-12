package com.nosjeux.xoudouqi.bo;

import java.time.LocalDateTime;

// Classe GameResult pour l'historique
public class GameResult {
    private int id;
    private final int player1Id;
    private final int player2Id;
    private final String player1Name;
    private final String player2Name;
    private int winnerId;
    private LocalDateTime gameDate;
    private int movesCount;

    public GameResult(int player1Id, int player2Id, String player1Name,
                      String player2Name, int winnerId, int movesCount) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.winnerId = winnerId;
        this.movesCount = movesCount;
        this.gameDate = LocalDateTime.now();
    }

    // Constructeur complet pour la lecture depuis DB
    public GameResult(int id, int player1Id, int player2Id, String player1Name,
                      String player2Name, int winnerId, LocalDateTime gameDate, int movesCount) {
        this.id = id;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.winnerId = winnerId;
        this.gameDate = gameDate;
        this.movesCount = movesCount;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getPlayer1Id() {
        return player1Id;
    }

    public int getPlayer2Id() {
        return player2Id;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public LocalDateTime getGameDate() {
        return gameDate;
    }

    public int getMovesCount() {
        return movesCount;
    }

    public String getResult() {
        if (winnerId == 0)
            return "Match nul";
        return winnerId == player1Id ? player1Name + " a gagné" : player2Name + " a gagné";
    }

    @Override
    public String toString() {
        return String.format("%s vs %s - %s (%d coups) - %s",
                player1Name, player2Name, getResult(),
                movesCount, gameDate.toString());
    }
}
