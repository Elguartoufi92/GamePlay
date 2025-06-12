package com.nosjeux.xoudouqi.bll;

import com.nosjeux.xoudouqi.bo.GameResult;
import com.nosjeux.xoudouqi.bo.Player;
import com.nosjeux.xoudouqi.dal.Database;

// 3. GameResultService - Gestion des résultats
public class GameResultService {
    private Database database;

    public GameResultService(Database database) {
        this.database = database;
    }

    public void saveGameResult(Player winner, Player loser, int moveCount, boolean surrender) {
        // Mettre à jour les statistiques
        winner.incrementWins();
        loser.incrementLosses();

        // Sauvegarder le résultat
        GameResult result = new GameResult(
                winner.getId(), loser.getId(),
                winner.getUsername(), loser.getUsername(),
                winner.getId(), moveCount
        );

        database.saveGameResult(result);
        database.updatePlayerStats(winner);
        database.updatePlayerStats(loser);
    }
}

