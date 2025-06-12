package com.nosjeux.xoudouqi.dal;

import com.nosjeux.xoudouqi.bo.GameResult;
import com.nosjeux.xoudouqi.bo.Player;

import java.sql.*;
import java.util.*;

// Classe Database pour la gestion de la base de données
public class Database {
    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\Pc\\Desktop\\XouDouQi\\database\\xoudouqi.db";
    private Connection connection;

    public Database() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            initializeTables();
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
        }
    }

    private void initializeTables() throws SQLException {
        String createPlayersTable = """
                    CREATE TABLE IF NOT EXISTS players (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL,
                        wins INTEGER DEFAULT 0,
                        losses INTEGER DEFAULT 0,
                        draws INTEGER DEFAULT 0,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                    )
                """;

        String createGamesTable = """
                    CREATE TABLE IF NOT EXISTS games (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        player1_id INTEGER NOT NULL,
                        player2_id INTEGER NOT NULL,
                        winner_id INTEGER DEFAULT 0,
                        moves_count INTEGER DEFAULT 0,
                        game_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (player1_id) REFERENCES players (id),
                        FOREIGN KEY (player2_id) REFERENCES players (id),
                        FOREIGN KEY (winner_id) REFERENCES players (id)
                    )
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createPlayersTable);
            stmt.execute(createGamesTable);
        }
    }

    public Player createPlayer(String username, String password) {
        String sql = "INSERT INTO players (username, password) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de la création du joueur");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Player player = new Player(username, password);
                    player.setId(generatedKeys.getInt(1));
                    return player;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du joueur: " + e.getMessage());
        }
        return null;
    }

    public Player authenticatePlayer(String username, String password) {
        String sql = "SELECT * FROM players WHERE username = ? AND password = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Player(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getInt("wins"),
                            rs.getInt("losses"),
                            rs.getInt("draws"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification: " + e.getMessage());
        }
        return null;
    }

    public boolean playerExists(String username) {
        String sql = "SELECT COUNT(*) FROM players WHERE username = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du joueur: " + e.getMessage());
        }
        return false;
    }

    public void updatePlayerStats(Player player) {
        String sql = "UPDATE players SET wins = ?, losses = ?, draws = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, player.getWins());
            pstmt.setInt(2, player.getLosses());
            pstmt.setInt(3, player.getDraws());
            pstmt.setInt(4, player.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour des statistiques: " + e.getMessage());
        }
    }

    public void saveGameResult(GameResult result) {
        String sql = "INSERT INTO games (player1_id, player2_id, winner_id, moves_count) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, result.getPlayer1Id());
            pstmt.setInt(2, result.getPlayer2Id());
            pstmt.setInt(3, result.getWinnerId());
            pstmt.setInt(4, result.getMovesCount());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement du résultat: " + e.getMessage());
        }
    }

    public List<GameResult> getPlayerHistory(int playerId, int limit) {
        List<GameResult> history = new ArrayList<>();
        String sql = """
                    SELECT g.*, p1.username as player1_name, p2.username as player2_name
                    FROM games g
                    JOIN players p1 ON g.player1_id = p1.id
                    JOIN players p2 ON g.player2_id = p2.id
                    WHERE g.player1_id = ? OR g.player2_id = ?
                    ORDER BY g.game_date DESC
                    LIMIT ?
                """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, playerId);
            pstmt.setInt(2, playerId);
            pstmt.setInt(3, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    GameResult result = new GameResult(
                            rs.getInt("id"),
                            rs.getInt("player1_id"),
                            rs.getInt("player2_id"),
                            rs.getString("player1_name"),
                            rs.getString("player2_name"),
                            rs.getInt("winner_id"),
                            rs.getTimestamp("game_date").toLocalDateTime(),
                            rs.getInt("moves_count"));
                    history.add(result);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'historique: " + e.getMessage());
        }

        return history;
    }

    public List<Player> getTopPlayers(int limit) {
        List<Player> topPlayers = new ArrayList<>();
        String sql = """
                    SELECT * FROM players
                    WHERE (wins + losses + draws) > 0
                    ORDER BY
                        CASE WHEN (wins + losses + draws) = 0 THEN 0
                             ELSE CAST(wins AS REAL) / (wins + losses + draws)
                        END DESC,
                        wins DESC
                    LIMIT ?
                """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Player player = new Player(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getInt("wins"),
                            rs.getInt("losses"),
                            rs.getInt("draws"));
                    topPlayers.add(player);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du classement: " + e.getMessage());
        }

        return topPlayers;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la base de données: " + e.getMessage());
        }
    }
}
