package com.nosjeux.xoudouqi.bll;

// 4. MoveValidationService - Validation des mouvements

import com.nosjeux.xoudouqi.bo.Case;
import com.nosjeux.xoudouqi.bo.GameBoard;
import com.nosjeux.xoudouqi.bo.Piece;
import com.nosjeux.xoudouqi.bo.Position;


public class MoveValidationService {

    public static class MoveResult {
        private boolean valid;
        private String errorMessage;

        public MoveResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }
    }

    public MoveResult validateMove(GameBoard board, Position from, Position to, int currentPlayer) {
        Case fromCase = board.getCase(from);
        Case toCase = board.getCase(to);

        if (fromCase == null || toCase == null) {
            return new MoveResult(false, "Position hors du plateau !");
        }

        if (fromCase.isEmpty()) {
            return new MoveResult(false, "Aucune pièce à cette position !");
        }

        Piece piece = fromCase.getPiece();
        if (piece.getPlayerId() != currentPlayer) {
            return new MoveResult(false, "Cette pièce ne vous appartient pas !");
        }

        if (!toCase.isEmpty() && toCase.getPiece().getPlayerId() == currentPlayer) {
            return new MoveResult(false, "Vous ne pouvez pas capturer vos propres pièces !");
        }

        return new MoveResult(true, null);
    }

    public Position parsePosition(String posStr) throws NumberFormatException {
        String[] coords = posStr.split(",");
        if (coords.length != 2) {
            throw new NumberFormatException("Format incorrect");
        }

        return new Position(
                Integer.parseInt(coords[0]),
                Integer.parseInt(coords[1])
        );
    }
}