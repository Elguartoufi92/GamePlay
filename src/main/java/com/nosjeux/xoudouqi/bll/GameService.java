package com.nosjeux.xoudouqi.bll;

import com.nosjeux.xoudouqi.bo.GameBoard;
import com.nosjeux.xoudouqi.bo.Player;
import com.nosjeux.xoudouqi.bo.Position;

// 1. GameService - Logique métier du jeu
public class GameService {
    private GameBoard board;
    private Player player1;
    private Player player2;
    private int currentPlayer;
    private int moveCount;

    public GameService() {
        this.currentPlayer = 1;
        this.moveCount = 0;
    }

    public void initializeGame(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        this.board = new GameBoard();
        this.currentPlayer = 1;
        this.moveCount = 0;
    }

    public boolean makeMove(Position from, Position to) {
        if (board.makeMove(from, to, currentPlayer)) {
            moveCount++;
            return true;
        }
        return false;
    }

    public int checkWinner() {
        return board.checkWin();
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer == 1 ? 2 : 1;
    }

    public Player getCurrentPlayer() {
        return currentPlayer == 1 ? player1 : player2;
    }

    public GameBoard getBoard() {
        return board;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getCurrentPlayerNumber() {
        return currentPlayer;
    }
}

