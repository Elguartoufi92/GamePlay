package com.nosjeux.xoudouqi.gui;

// 3. GameView - Interface du jeu

import com.nosjeux.xoudouqi.bo.GameBoard;
import com.nosjeux.xoudouqi.bo.Player;

import java.util.Scanner;

public class GameView {
    private Scanner scanner;

    public GameView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showGameStart(Player player1, Player player2) {
        System.out.println("\n=== DÉBUT DE LA PARTIE ===");
        System.out.println(player1.getUsername() + " (pièces minuscules) vs " +
                player2.getUsername() + " (pièces MAJUSCULES)");
    }

    public void showGameBoard(GameBoard board) {
        System.out.println("\n" + "=".repeat(50));
        board.displayBoard();
    }

    public void showPlayerTurn(Player player, int playerNumber) {
        System.out.println("\n\nTour de " + player.getUsername() +
                " (Joueur " + playerNumber + ")");
        System.out.println("Commandes: 'move x1,y1 x2,y2', 'help', 'surrender', 'save'");
    }

    public String getPlayerInput() {
        System.out.print(">>> ");
        return scanner.nextLine().trim().toLowerCase();
    }

    public void showMoveSuccess() {
        System.out.println("Mouvement effectué !");
    }

    public void showMoveError(String error) {
        System.out.println("Mouvement invalide ! " + error);
    }

    public void showInvalidCommand() {
        System.out.println("Commande inconnue ! Tapez 'help' pour l'aide.");
    }

    public void showSaveNotImplemented() {
        System.out.println("Fonctionnalité de sauvegarde non implémentée dans cette version.");
    }

    public void showGameEnd(Player winner, Player loser, int moveCount, boolean surrender) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("=== FIN DE PARTIE ===");

        if (surrender) {
            System.out.println(loser.getUsername() + " abandonne !");
            System.out.println(winner.getUsername() + " remporte la victoire !");
        } else {
            System.out.println(winner.getUsername() + " a atteint le sanctuaire adverse !");
            System.out.println("Victoire en " + moveCount + " coups !");
        }

        System.out.println("\nStatistiques mises à jour !");
    }
}
