package com.nosjeux.xoudouqi.gui;

// 2. PlayerView - Interface de gestion des joueurs

import com.nosjeux.xoudouqi.bo.Player;

import java.util.Scanner;

public class PlayerView {
    private Scanner scanner;

    public PlayerView(Scanner scanner) {
        this.scanner = scanner;
    }

    public int showPlayerMenu(String playerLabel) {
        System.out.println("\n--- " + playerLabel + " ---");
        System.out.println("1. Se connecter");
        System.out.println("2. Créer un compte");

        return getIntInput("Votre choix: ");
    }

    public String[] getLoginCredentials() {
        System.out.print("Nom d'utilisateur: ");
        String username = scanner.nextLine().trim();

        System.out.print("Mot de passe: ");
        String password = scanner.nextLine().trim();

        return new String[]{username, password};
    }

    public String[] getRegistrationInfo() {
        System.out.print("Nom d'utilisateur: ");
        String username = scanner.nextLine().trim();

        System.out.print("Mot de passe: ");
        String password = scanner.nextLine().trim();

        return new String[]{username, password};
    }

    public void showLoginSuccess(String username) {
        System.out.println("Connexion réussie ! Bienvenue " + username);
    }

    public void showLoginFailure() {
        System.out.println("Nom d'utilisateur ou mot de passe incorrect !");
    }

    public void showRegistrationSuccess(String username) {
        System.out.println("Compte créé avec succès ! Bienvenue " + username);
    }

    public void showRegistrationFailure(String reason) {
        System.out.println("Erreur lors de la création du compte : " + reason);
    }

    public void showStatistics(Player player) {
        System.out.println("\n=== STATISTIQUES DE " + player.getUsername().toUpperCase() + " ===");
        System.out.println("Victoires: " + player.getWins());
        System.out.println("Défaites: " + player.getLosses());
        System.out.println("Matchs nuls: " + player.getDraws());
        System.out.println("Total parties: " + player.getTotalGames());
        System.out.println("Taux de victoire: " + String.format("%.1f%%", player.getWinRate()));
    }

    public void showBothPlayersStats(Player player1, Player player2) {
        System.out.println("\n=== STATISTIQUES ===");
        System.out.println("Joueur 1 - " + player1);
        System.out.println("Joueur 2 - " + player2);
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide !");
            }
        }
    }
}
