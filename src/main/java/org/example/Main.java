package org.example;

import org.example.Game.Menus.Menu;

public class Main {
    public static void main(String[] args) {
        String missionFile = "mission.json";

        try {
            Menu menu = new Menu(missionFile);
            menu.displayMenu();
        } catch (Exception e) {
            System.err.println("Ocorreu um erro ao iniciar o jogo.");
            e.printStackTrace();
        }
    }
}

