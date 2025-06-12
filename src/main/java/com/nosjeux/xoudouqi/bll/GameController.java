package com.nosjeux.xoudouqi.bll;

import com.nosjeux.xoudouqi.bo.GameResult;
import com.nosjeux.xoudouqi.bo.Player;
import com.nosjeux.xoudouqi.bo.Position;
import com.nosjeux.xoudouqi.dal.Database;
import com.nosjeux.xoudouqi.gui.*;

import java.util.List;
import java.util.Scanner;

public class GameController {
    private Scanner scanner;
    private Database database;

    // Services
    private GameService gameService;
    private PlayerService playerService;
    private GameResultService gameResultService;
    private MoveValidationService moveValidationService;

    // Vues
    private MainMenuView mainMenuView;
    private PlayerView playerView;
    private GameView gameView;
    private HelpView helpView;
    private StatisticsView statisticsView;

    public GameController() {
        scanner = new Scanner(System.in);
        database = new Database();

        // Initialisation des services
        gameService = new GameService();
        playerService = new PlayerService(database);
        gameResultService = new GameResultService(database);
        moveValidationService = new MoveValidationService();

        // Initialisation des vues
        mainMenuView = new MainMenuView(scanner);
        playerView = new PlayerView(scanner);
        gameView = new GameView(scanner);
        helpView = new HelpView();
        statisticsView = new StatisticsView();
    }

    public void start() {
        mainMenuView.showWelcome();

        while (true) {
            int choice = mainMenuView.showMainMenu();

            switch (choice) {
                case 1 -> startNewGame();
                case 2 -> showStatistics();
                case 3 -> showHistory();
                case 4 -> showRanking();
                case 5 -> helpView.showRules();
                case 6 -> {
                    mainMenuView.showExitMessage();
                    database.close();
                    return;
                }
                default -> mainMenuView.showInvalidChoice();
            }
        }
    }

    private void startNewGame() {
        // 1. Authentification des joueurs
        Player player1 = handlePlayerLogin("Joueur 1");
        if (player1 == null) return;

        Player player2 = handlePlayerLogin("Joueur 2");
        if (player2 == null) return;

        // 2. Initialisation du jeu via le service
        gameService.initializeGame(player1, player2);

        // 3. Affichage du début de partie
        gameView.showGameStart(player1, player2);

        // 4. Boucle de jeu principale
        playGameLoop();
    }

    private Player handlePlayerLogin(String playerLabel) {
        int choice = playerView.showPlayerMenu(playerLabel);

        switch (choice) {
            case 1 -> {
                return loginPlayer();
            }
            case 2 -> {
                return createPlayer();
            }
            default -> {
                playerView.showRegistrationFailure("Choix invalide !");
                return null;
            }
        }
    }

    private Player loginPlayer() {
        String[] credentials = playerView.getLoginCredentials();
        String username = credentials[0];
        String password = credentials[1];

        Player player = playerService.authenticatePlayer(username, password);
        if (player != null) {
            playerView.showLoginSuccess(username);
        } else {
            playerView.showLoginFailure();
        }

        return player;
    }

    private Player createPlayer() {
        String[] info = playerView.getRegistrationInfo();
        String username = info[0];
        String password = info[1];

        if (username.isEmpty()) {
            playerView.showRegistrationFailure("Le nom d'utilisateur ne peut pas être vide !");
            return null;
        }

        if (password.isEmpty()) {
            playerView.showRegistrationFailure("Le mot de passe ne peut pas être vide !");
            return null;
        }

        if (playerService.playerExists(username)) {
            playerView.showRegistrationFailure("Ce nom d'utilisateur existe déjà !");
            return null;
        }

        Player player = playerService.createPlayer(username, password);
        if (player != null) {
            playerView.showRegistrationSuccess(username);
        } else {
            playerView.showRegistrationFailure("Erreur lors de la création du compte !");
        }

        return player;
    }

    private void playGameLoop() {
        while (true) {
            // Affichage du plateau et du tour actuel
            gameView.showGameBoard(gameService.getBoard());
            gameView.showPlayerTurn(gameService.getCurrentPlayer(), gameService.getCurrentPlayerNumber());

            // Récupération de la commande du joueur
            String input = gameView.getPlayerInput();

            // Traitement des commandes
            if (input.equals("help")) {
                helpView.showGameHelp();
                continue;
            }

            if (input.equals("surrender")) {
                handleSurrender();
                return;
            }
            if (input.startsWith("move ")) {
                if (handleMove(input)) {
                    // Vérifier les conditions de victoire
                    int winner = gameService.checkWinner();
                    if (winner != 0) {
                        gameView.showGameBoard(gameService.getBoard());
                        handleGameEnd(winner, false);
                        return;
                    }

                    // Changer de joueur
                    gameService.switchPlayer();
                }
            } else {
                gameView.showInvalidCommand();
            }
        }
    }

    private boolean handleMove(String input) {
        try {
            // Parse de la commande "move x1,y1 x2,y2"
            String[] parts = input.split(" ");
            if (parts.length != 3) {
                gameView.showMoveError("Format incorrect ! Utilisez: move x1,y1 x2,y2");
                return false;
            }

            Position from = moveValidationService.parsePosition(parts[1]);
            Position to = moveValidationService.parsePosition(parts[2]);

            // Validation du mouvement
            MoveValidationService.MoveResult validation =
                    moveValidationService.validateMove(gameService.getBoard(), from, to, gameService.getCurrentPlayerNumber());

            if (!validation.isValid()) {
                gameView.showMoveError(validation.getErrorMessage());
                return false;
            }

            // Exécution du mouvement
            if (gameService.makeMove(from, to)) {
                gameView.showMoveSuccess();
                return true;
            } else {
                gameView.showMoveError("Mouvement non autorisé pour cette pièce !");
                return false;
            }

        } catch (NumberFormatException e) {
            gameView.showMoveError("Coordonnées invalides ! Utilisez des nombres entiers.");
            return false;
        } catch (Exception e) {
            gameView.showMoveError("Erreur lors du traitement du mouvement: " + e.getMessage());
            return false;
        }
    }

    private void handleSurrender() {
        handleGameEnd(gameService.getCurrentPlayerNumber() == 1 ? 2 : 1, true);
    }

    private void handleGameEnd(int winnerNumber, boolean surrender) {
        Player winner = winnerNumber == 1 ? gameService.getPlayer1() : gameService.getPlayer2();
        Player loser = winnerNumber == 1 ? gameService.getPlayer2() : gameService.getPlayer1();

        // Affichage de la fin de partie
        gameView.showGameEnd(winner, loser, gameService.getMoveCount(), surrender);

        // Sauvegarde du résultat via le service
        gameResultService.saveGameResult(winner, loser, gameService.getMoveCount(), surrender);
    }

    private void showStatistics() {
        // Si on est en cours de partie, afficher les stats des deux joueurs
        if (gameService.getPlayer1() != null && gameService.getPlayer2() != null) {
            playerView.showBothPlayersStats(gameService.getPlayer1(), gameService.getPlayer2());
        } else {
            // Sinon, demander de se connecter pour voir ses stats
            Player player = handlePlayerLogin("Consultation des statistiques");
            if (player != null) {
                playerView.showStatistics(player);
            }
        }
    }

    private void showHistory() {
        Player player = handlePlayerLogin("Consultation de l'historique");
        if (player == null) return;

        List<GameResult> history = playerService.getPlayerHistory(player.getId(), 10);
        statisticsView.showPlayerHistory(player, history);
    }

    private void showRanking() {
        List<Player> topPlayers = playerService.getTopPlayers(10);
        statisticsView.showRanking(topPlayers);
    }
    }

