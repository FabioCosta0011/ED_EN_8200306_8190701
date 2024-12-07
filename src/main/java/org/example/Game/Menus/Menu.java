package org.example.Game.Menus;

import org.example.Game.Entities.ENUMS.DifficultyType;
import org.example.Game.Entities.Game;

import java.io.IOException;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private Game game;
    private String missionFile;

    public Menu(String missionFile) {
        this.missionFile = missionFile;
    }

    // Start the menu system
    public void displayMenu() {
        while (true) {
            printMainMenu();
            int choice = getValidMenuChoice();
            handleMainMenuChoice(choice);
        }
    }

    // Print the main menu
    private void printMainMenu() {
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("                       MENU                         ");
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("1. Start Game");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    // Get a valid menu choice from the user
    private int getValidMenuChoice() {
        while (true) {
            try {
                // Leitura da entrada e exibindo para debug
                String input = scanner.nextLine().trim();
                System.out.println("You entered: " + input);  // Debug line

                int choice = Integer.parseInt(input);  // Tenta converter a entrada para número

                // Verifica se o número está dentro do intervalo permitido
                if (choice == 1 || choice == 2 || choice == 3 || choice == 0) {
                    return choice;
                } else {
                    System.out.println("Invalid choice! Please select a valid option.");
                }
            } catch (NumberFormatException e) {

                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    // Handle the main menu choice
    private void handleMainMenuChoice(int choice) {
        if (choice == 1) {
            start();  // Start the game and load mission details
        } else {
            System.out.println("Exiting...");
            System.exit(0);
        }
    }

    // Start the game and show the in-game menu
    private void start() {
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("                       STARTING                     ");
        System.out.println("════════════════════════════════════════════════════");

        loadMissionAndShowDetails();
        showDifficultySelection();
    }

    // Load the mission and show details
    private void loadMissionAndShowDetails() {
        try {
            game = new Game(this.missionFile, this.scanner);
            game.loadMissionFromJson();
            game.displayMissionInfo();
        } catch (IOException e) {
            System.err.println("Error loading mission: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Show the difficulty selection
    private void showDifficultySelection() {
        DifficultyType difficulty = getDifficultyFromUser();
        createGameWithDifficulty(difficulty);
    }

    // Get difficulty input from the user
    private DifficultyType getDifficultyFromUser() {
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("                    SELECT DIFFICULTY               ");
        System.out.println("════════════════════════════════════════════════════");

        int difficultyInput = getValidDifficultyInput();
        return mapInputToDifficulty(difficultyInput);
    }

    // Get a valid difficulty input
    private int getValidDifficultyInput() {
        while (true) {
            System.out.print("Choose difficulty level (1 - Easy, 2 - Medium, 3 - Hard): ");
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= 1 && input <= 3) {
                    return input;
                } else {
                    System.out.println("Invalid difficulty level. Please select 1, 2, or 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    // Map the input to a DifficultyType
    private DifficultyType mapInputToDifficulty(int input) {
        switch (input) {
            case 1: return DifficultyType.EASY;
            case 2: return DifficultyType.MEDIUM;
            case 3: return DifficultyType.HARD;
            default:
                System.out.println("Invalid input! Defaulting to Medium difficulty.");
                return DifficultyType.MEDIUM;
        }
    }

    // Create the game with the selected difficulty
    private void createGameWithDifficulty(DifficultyType difficulty) {
        System.out.println("Loading game with difficulty: " + difficulty);
        try {
            game.createToCruzCharacter(difficulty);
            displayInGameMenu();  // Show the in-game menu after creating ToCruz
        } catch (IOException e) {
            System.err.println("Error creating ToCruz character: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Display the in-game menu with options
    private void displayInGameMenu() {
        boolean running = true;
        while (running) {
            printInGameMenu();
            int choice = getValidMenuChoice();
            running = handleInGameMenuChoice(choice);
        }
    }

    // Print the in-game menu
    private void printInGameMenu() {
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("                   IN-GAME MENU                     ");
        System.out.println("════════════════════════════════════════════════════");
        game.displayMissionInfo();
        System.out.println("1. Start the Game");
        System.out.println("2. View ToCruz Info");
        System.out.println("3. View Mission Info");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    // Handle the in-game menu choice
    private boolean handleInGameMenuChoice(int choice) {
        switch (choice) {

            case 1:
                startGame();
                return false;
            case 2:
                game.displayToCruzDetails();
                return true;
            case 3:
                game.displayBuildingDetails();
                return true;
            case 0:
                System.out.println("Exiting...");
                System.exit(0);
                return false;
            default:
                System.out.println("Invalid choice! Please select a valid option.");
                return true;
        }
    }

    // Stub method for starting the game (to be implemented)
    private void startGame() {
        System.out.println("Game started... (this is a stub, implement the game logic here).");
    }
}