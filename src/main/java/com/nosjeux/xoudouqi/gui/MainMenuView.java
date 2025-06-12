package com.nosjeux.xoudouqi.gui;

// 1. MainMenuView - Interface du menu principal

import java.util.Scanner;

public class MainMenuView {
    private Scanner scanner;

    public MainMenuView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showWelcome() {
        System.out.println("=== Bienvenue dans Xou Dou Qi - Le Jeu de la Jungle ===\n");
    }

    public int showMainMenu() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Nouvelle partie");
        System.out.println("2. Mes statistiques");
        System.out.println("3. Historique des parties");
        System.out.println("4. Classement");
        System.out.println("5. Règles du jeu");
        System.out.println("6. Quitter");

        return getIntInput("Votre choix: ");
    }

    public void showExitMessage() {
        System.out.println("Merci d'avoir joué !");
    }

    public void showInvalidChoice() {
        System.out.println("Choix invalide !");
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
