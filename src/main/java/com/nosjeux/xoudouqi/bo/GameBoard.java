package com.nosjeux.xoudouqi.bo;
// Classe principale du plateau
public class GameBoard {
    private static final int WIDTH = 7;
    private static final int HEIGHT = 9;
    private Case[][] board;

    public GameBoard() {
        initializeBoard();
        placePieces();
    }

    private void initializeBoard() {
        board = new Case[HEIGHT][WIDTH];

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Position pos = new Position(x, y);
                CaseType type = determineCaseType(x, y);
                board[y][x] = new Case(type, pos);
            }
        }
    }

    private CaseType determineCaseType(int x, int y) {
        // Sanctuaires
        if ((x == 3 && y == 0) || (x == 3 && y == 8)) {
            return CaseType.SANCTUARY;
        }

        // Pièges (autour des sanctuaires)
        if ((x == 2 && y == 0) || (x == 4 && y == 0) || (x == 3 && y == 1) ||
                (x == 2 && y == 8) || (x == 4 && y == 8) || (x == 3 && y == 7)) {
            return CaseType.TRAP;
        }

        // Lacs (au centre)
        if ((x >= 1 && x <= 2 && y >= 3 && y <= 5) ||
                (x >= 4 && x <= 5 && y >= 3 && y <= 5)) {
            return CaseType.WATER;
        }

        return CaseType.NORMAL;
    }

    private void placePieces() {
        // Placement initial des pièces du joueur 1 (en haut)
        placePiece(new Piece(Animal.RAT, 1, new Position(6, 2)));
        placePiece(new Piece(Animal.CAT, 1, new Position(1, 1)));
        placePiece(new Piece(Animal.DOG, 1, new Position(5, 1)));
        placePiece(new Piece(Animal.WOLF, 1, new Position(2, 2)));
        placePiece(new Piece(Animal.PANTHER, 1, new Position(4, 2)));
        placePiece(new Piece(Animal.TIGER, 1, new Position(0, 0)));
        placePiece(new Piece(Animal.LION, 1, new Position(6, 0)));
        placePiece(new Piece(Animal.ELEPHANT, 1, new Position(0, 2)));

        // Placement initial des pièces du joueur 2 (en bas)
        placePiece(new Piece(Animal.RAT, 2, new Position(0, 6)));
        placePiece(new Piece(Animal.CAT, 2, new Position(5, 7)));
        placePiece(new Piece(Animal.DOG, 2, new Position(1, 7)));
        placePiece(new Piece(Animal.WOLF, 2, new Position(4, 6)));
        placePiece(new Piece(Animal.PANTHER, 2, new Position(2, 6)));
        placePiece(new Piece(Animal.TIGER, 2, new Position(6, 8)));
        placePiece(new Piece(Animal.LION, 2, new Position(0, 8)));
        placePiece(new Piece(Animal.ELEPHANT, 2, new Position(6, 6)));
    }

    private void placePiece(Piece piece) {
        Position pos = piece.getPosition();
        board[pos.getY()][pos.getX()].setPiece(piece);
    }

    public Case getCase(Position position) {
        if (!position.isValid()) return null;
        return board[position.getY()][position.getX()];
    }

    public boolean isValidMove(Position from, Position to, int playerId) {
        Case fromCase = getCase(from);
        Case toCase = getCase(to);

        if (fromCase == null || toCase == null) return false;
        if (fromCase.isEmpty()) return false;

        Piece piece = fromCase.getPiece();
        if (piece.getPlayerId() != playerId) return false;

        // Vérification des mouvements (adjacent seulement)
        int dx = Math.abs(to.getX() - from.getX());
        int dy = Math.abs(to.getY() - from.getY());

        // Mouvement normal (une case adjacente)
        if ((dx == 1 && dy == 0) || (dx == 0 && dy == 1)) {
            return canMoveToCase(piece, toCase);
        }

        // Saut pour Lion et Tigre
        if (piece.getAnimal() == Animal.LION || piece.getAnimal() == Animal.TIGER) {
            return canJump(from, to);
        }

        return false;
    }

    private boolean canMoveToCase(Piece piece, Case targetCase) {
        // Vérifier si la case est accessible
        if (!targetCase.isAccessibleTo(piece.getAnimal())) {
            return false;
        }

        // Vérifier le sanctuaire (ne peut pas entrer dans le sien)
        if (targetCase.getType() == CaseType.SANCTUARY) {
            Position sanctuaryPos = targetCase.getPosition();
            // Joueur 1 ne peut pas entrer dans le sanctuaire en (3,0)
            // Joueur 2 ne peut pas entrer dans le sanctuaire en (3,8)
            if ((piece.getPlayerId() == 1 && sanctuaryPos.getY() == 0) ||
                    (piece.getPlayerId() == 2 && sanctuaryPos.getY() == 8)) {
                return false;
            }
        }

        // Case vide ou contient une pièce capturable
        if (targetCase.isEmpty()) {
            return true;
        }

        Piece targetPiece = targetCase.getPiece();
        if (targetPiece.getPlayerId() == piece.getPlayerId()) {
            return false; // Ne peut pas capturer ses propres pièces
        }

        // Vérifier si dans un piège
        boolean targetInTrap = (targetCase.getType() == CaseType.TRAP &&
                targetPiece.getPlayerId() != piece.getPlayerId());

        return piece.canCapture(targetPiece, targetInTrap);
    }

    private boolean canJump(Position from, Position to) {
        // Implémentation du saut pour Lion et Tigre
        // Ils peuvent sauter par-dessus l'eau sauf si un rat s'y trouve

        int dx = to.getX() - from.getX();
        int dy = to.getY() - from.getY();

        // Saut horizontal ou vertical seulement
        if (dx != 0 && dy != 0) return false;

        // Vérifier s'il y a de l'eau entre les positions
        int stepX = Integer.signum(dx);
        int stepY = Integer.signum(dy);

        Position current = new Position(from.getX() + stepX, from.getY() + stepY);
        while (!current.equals(to)) {
            Case currentCase = getCase(current);
            if (currentCase == null) return false;

            if (currentCase.getType() == CaseType.WATER) {
                // S'il y a un rat dans l'eau, ne peut pas sauter
                if (!currentCase.isEmpty() &&
                        currentCase.getPiece().getAnimal() == Animal.RAT) {
                    return false;
                }
            }

            current = new Position(current.getX() + stepX, current.getY() + stepY);
        }

        return true;
    }

    public boolean makeMove(Position from, Position to, int playerId) {
        if (!isValidMove(from, to, playerId)) {
            return false;
        }

        Case fromCase = getCase(from);
        Case toCase = getCase(to);

        Piece piece = fromCase.getPiece();

        // Effectuer le mouvement
        fromCase.setPiece(null);
        toCase.setPiece(piece);
        piece.setPosition(to);

        // Mettre à jour le statut dans l'eau
        piece.setInWater(toCase.getType() == CaseType.WATER);

        return true;
    }

    public int checkWin() {
        // Vérifier si un joueur a atteint le sanctuaire adverse
        Case sanctuary1 = getCase(new Position(3, 8)); // Sanctuaire joueur 2
        Case sanctuary2 = getCase(new Position(3, 0)); // Sanctuaire joueur 1

        if (!sanctuary1.isEmpty() && sanctuary1.getPiece().getPlayerId() == 1) {
            return 1; // Joueur 1 gagne
        }

        if (!sanctuary2.isEmpty() && sanctuary2.getPiece().getPlayerId() == 2) {
            return 2; // Joueur 2 gagne
        }

        return 0; // Pas de gagnant
    }

    public void displayBoard() {
        System.out.println("    0   1   2   3   4   5   6\n  ___________________________");
        for (int y = 0; y < HEIGHT; y++) {
            System.out.print(y + " | ");
            for (int x = 0; x < WIDTH; x++) {
                Case c = board[y][x];
                if (!c.isEmpty()) {
                    Piece p = c.getPiece();
                    System.out.print(getAnimalSymbol(p.getAnimal(), p.getPlayerId()));
                } else {
                    System.out.print(getCaseSymbol(c.getType()));
                }
                System.out.print(" ");
            }
            if(y != HEIGHT-1) {
                System.out.println("\n  |");
            }
        }
    }

    private String getAnimalSymbol(Animal animal, int playerId) {
        String symbol = switch (animal) {
            case ELEPHANT -> " E ";
            case LION -> " L ";
            case TIGER -> " T ";
            case PANTHER -> " P ";
            case DOG -> " D ";
            case WOLF -> " W ";
            case CAT -> " C ";
            case RAT -> " R ";
        };
        return playerId == 1 ? symbol.toLowerCase() : symbol.toUpperCase();
    }

    private String getCaseSymbol(CaseType type) {
        return switch (type) {
            case WATER -> " ~ ";
            case TRAP -> " x ";
            case SANCTUARY -> " # ";
            default -> " . ";
        };
    }
}

