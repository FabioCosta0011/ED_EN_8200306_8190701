package org.example;

import org.example.Game.Menus.Menu;

public class Main {
    public static void main(String[] args) {
        String missionFile = "mission.json";
        Menu menu = new Menu(missionFile);
        menu.displayMenu();
    }
}

