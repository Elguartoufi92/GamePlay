package com.nosjeux.xoudouqi.bo;

// 5. Classe Case
public class Case {
    private final CaseType type;
    private final Position position;
    private Piece piece;

    public Case(CaseType type, Position position) {
        this.type = type;
        this.position = position;
        this.piece = null;
    }

    // Getters et setters
    public CaseType getType() { return type; }
    public Position getPosition() { return position; }
    public Piece getPiece() { return piece; }
    public void setPiece(Piece piece) { this.piece = piece; }
    public boolean isEmpty() { return piece == null; }

    public boolean isAccessibleTo(Animal animal) {
        switch (type) {
            case WATER:
                return animal == Animal.RAT;
            case SANCTUARY:
                // Ne peut pas entrer dans son propre sanctuaire
                return true; // Vérification faite au niveau du plateau
            default:
                return true;
        }
    }
}
