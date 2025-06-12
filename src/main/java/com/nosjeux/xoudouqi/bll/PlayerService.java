package com.nosjeux.xoudouqi.bll;

import com.nosjeux.xoudouqi.bo.GameResult;
import com.nosjeux.xoudouqi.bo.Player;
import com.nosjeux.xoudouqi.dal.Database;

import java.util.List;

// 2. PlayerService - Gestion des joueurs
public class PlayerService {
    private Database database;

    public PlayerService(Database database) {
        this.database = database;
    }

    public Player authenticatePlayer(String username, String password) {
        return database.authenticatePlayer(username, password);
    }

    public Player createPlayer(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return null;
        }

        if (database.playerExists(username)) {
            return null;
        }

        return database.createPlayer(username, password);
    }

    public boolean playerExists(String username) {
        return database.playerExists(username);
    }

    public void updatePlayerStats(Player player) {
        database.updatePlayerStats(player);
    }

    public List<Player> getTopPlayers(int limit) {
        return database.getTopPlayers(limit);
    }

    public List<GameResult> getPlayerHistory(int playerId, int limit) {
        return database.getPlayerHistory(playerId, limit);
    }
}
