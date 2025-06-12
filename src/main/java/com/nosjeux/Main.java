package com.nosjeux;

import com.nosjeux.xoudouqi.bll.GameController;

public class Main {
    public static void main(String[] args) {
        try {
            GameController gameController = new GameController();
            gameController.start();
        } catch (Exception e) {
            System.err.println("Erreur fatale : " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
