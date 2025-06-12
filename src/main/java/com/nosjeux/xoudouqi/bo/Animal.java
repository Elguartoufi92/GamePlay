package com.nosjeux.xoudouqi.bo;

//  Enumération des animaux
public enum Animal {
    ELEPHANT(8, "Éléphant"),
    LION(7, "Lion"),
    TIGER(6, "Tigre"),
    PANTHER(5, "Panthère"),
    WOLF(4, "Loup"),
    DOG(3, "Chien"),
    CAT(2, "Chat"),
    RAT(1, "Rat");

    private final int rank;
    private final String name;

    Animal(int rank, String name) {
        this.rank = rank;
        this.name = name;
    }

    public int getRank() { return rank; }
    public String getName() { return name; }

    public boolean canCapture(Animal other) {
        // Le rat peut capturer l'éléphant (exception spéciale)
        if (this == RAT && other == ELEPHANT) return true;
        // Sinon, peut capturer si rang supérieur ou égal
        return this.rank >= other.rank;
    }
}
