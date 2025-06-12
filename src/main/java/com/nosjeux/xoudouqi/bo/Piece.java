package com.nosjeux.xoudouqi.bo;

public class Piece {
    private final Animal animal;
    private final int playerId; // 1 ou 2
    private Position position;
    private boolean inWater;

    public Piece(Animal animal, int playerId, Position position) {
        this.animal = animal;
        this.playerId = playerId;
        this.position = position;
        this.inWater = false;
    }

    // Getters et setters
    public Animal getAnimal() { return animal; }
    public int getPlayerId() { return playerId; }
    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
    public boolean isInWater() { return inWater; }
    public void setInWater(boolean inWater) { this.inWater = inWater; }

    public boolean canCapture(Piece other, boolean isInTrap) {
        if (isInTrap) {
            // Dans un piège, toutes les pièces peuvent être capturées
            return true;
        }
        return this.animal.canCapture(other.animal);
    }

    @Override
    public String toString() {
        return animal.getName() + " (J" + playerId + ")";
    }
}
