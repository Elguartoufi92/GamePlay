package com.nosjeux.xoudouqi.gui;

// 4. HelpView - Interface d'aide et règles

public class HelpView {

    public void showGameHelp() {
        System.out.println("\n=== AIDE DU JEU ===");
        System.out.println("Commandes disponibles:");
        System.out.println("  move x1,y1 x2,y2  - Déplacer une pièce de (x1,y1) vers (x2,y2)");
        System.out.println("  help              - Afficher cette aide");
        System.out.println("  surrender         - Abandonner la partie");
        System.out.println("  save              - Sauvegarder (non implémenté)");
        System.out.println("\nSymboles sur le plateau:");
        System.out.println("  . = case normale    ~ = eau       x = piège    # = sanctuaire");
        System.out.println("  Minuscules = Joueur 1    MAJUSCULES = Joueur 2");
        System.out.println("  E/e=Éléphant L/l=Lion T/t=Tigre P/p=Panthère");
        System.out.println("  D/d=Chien W/w=Loup C/c=Chat R/r=Rat");
        System.out.println("\nObjectif: Atteindre le sanctuaire adverse (#) avec n'importe quelle pièce");
    }

    public void showRules() {
        System.out.println("\n=== RÈGLES DU JEU XOU DOU QI ===");
        System.out.println("Le Xou Dou Qi (jeu de la jungle) est un jeu de stratégie chinois.");
        System.out.println("\nOBJECTIF:");
        System.out.println("- Être le premier à atteindre le sanctuaire adverse avec n'importe quelle pièce");
        System.out.println("\nPIÈCES (par ordre de force):");
        System.out.println("1. Éléphant (E) - Le plus fort");
        System.out.println("2. Lion (L)     - Peut sauter par-dessus l'eau");
        System.out.println("3. Tigre (T)    - Peut sauter par-dessus l'eau");
        System.out.println("4. Panthère (P)");
        System.out.println("5. Chien (D)");
        System.out.println("6. Loup (W)");
        System.out.println("7. Chat (C)");
        System.out.println("8. Rat (R)      - Seul à pouvoir aller dans l'eau, peut capturer l'éléphant");
        System.out.println("\nRÈGLES DE CAPTURE:");
        System.out.println("- Une pièce peut capturer une pièce de rang égal ou inférieur");
        System.out.println("- Exception: le Rat peut capturer l'Éléphant");
        System.out.println("- Dans un piège, toutes les pièces ennemies peuvent être capturées");
        System.out.println("\nCASES SPÉCIALES:");
        System.out.println("- Eau (~): Seul le Rat peut s'y déplacer");
        System.out.println("- Pièges (x): Affaiblissent les pièces ennemies");
        System.out.println("- Sanctuaires (#): Objectif à atteindre (pas le sien)");
        System.out.println("\nMOUVEMENTS:");
        System.out.println("- Une case horizontalement ou verticalement (pas en diagonale)");
        System.out.println("- Lion et Tigre peuvent sauter par-dessus l'eau (sauf si un Rat bloque)");
    }
}

