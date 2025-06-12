package com.nosjeux.xoudouqi.gui;

import com.nosjeux.xoudouqi.bo.GameResult;
import com.nosjeux.xoudouqi.bo.Player;

import java.util.List;

// 5. StatisticsView - Interface des statistiques et classements
public class StatisticsView {

    public void showPlayerHistory(Player player, List<GameResult> history) {
        System.out.println("\n=== HISTORIQUE DE " + player.getUsername().toUpperCase() + " ===");
        if (history.isEmpty()) {
            System.out.println("Aucune partie jouée.");
            return;
        }

        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + ". " + history.get(i));
        }
    }

    public void showRanking(List<Player> topPlayers) {
        System.out.println("\n=== CLASSEMENT ===");
        if (topPlayers.isEmpty()) {
            System.out.println("Aucun joueur dans le classement.");
            return;
        }

        for (int i = 0; i < topPlayers.size(); i++) {
            System.out.printf("%2d. %s%n", (i + 1), topPlayers.get(i));
        }
    }
}
