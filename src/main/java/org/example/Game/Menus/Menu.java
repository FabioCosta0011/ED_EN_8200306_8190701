package org.example.Game.Menus;

import org.example.Game.Entities.Division;
import org.example.Game.Entities.ENUMS.DifficultyType;
import org.example.Game.Entities.Game;
import org.example.Game.Entities.Mission;

import java.io.IOException;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private Game game;
    private String missionFile;
    private Division currentDivision;
    private boolean initialFight = true;


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
            case 1:
                return DifficultyType.EASY;
            case 2:
                return DifficultyType.MEDIUM;
            case 3:
                return DifficultyType.HARD;
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
            printGameMenu();
            int choice = getValidMenuChoice();
            running = handleGameMenuChoice(choice);
        }
    }

    // Print the game menu
    private void printGameMenu() {
        game.displayMissionInfo();
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("                      GAME MENU                     ");
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("1. Start the Game");
        System.out.println("2. View ToCruz Info");
        System.out.println("3. View Mission Info");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    // Handle the game menu choice
    private boolean handleGameMenuChoice(int choice) {
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

    // Get a valid InGame input
    private int getValidInGameMenuChoice() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                System.out.println("You entered: " + input);

                int choice = Integer.parseInt(input);

                if (choice == 1 || choice == 2 || choice == 3 || choice == 4 || choice == 5 || choice == 6 || choice == 7 || choice == 8 || choice == 9 || choice == 0) {
                    return choice;
                } else {
                    System.out.println("Invalid choice! Please select a valid option.");
                }
            } catch (NumberFormatException e) {

                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    // Method for starting the game
    private void startGame() {
        boolean running = true;

        while (running) {
            if (initialFight) {
                game.printInGameDivisionFight();
                initialFight = false;
            }

            printInGameMenu();
            int choice = getValidInGameMenuChoice();
            running = handleInGameMenuChoice(choice);
        }
    }

    // Print the in-game menu
    public void printInGameMenu() {
        if (game.toCruzDies()) {
            System.exit(0);
        }
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("                      IN-GAME MENU                  ");
        System.out.println("════════════════════════════════════════════════════");

        if (game.getToCruz() != null && game.getToCruz().getCurrentDivision() != null) {
            game.displayCurrentDivisionDetails();
        } else {
            System.out.println("Current Division: Not set");
        }

        System.out.println("════════════════════════════════════════════════════");
        System.out.println("1. Attack Enemies");
        System.out.println("2. Collect Target");
        System.out.println("3. Collect Items");
        System.out.println("4. ToCruz Info");
        System.out.println("5. Use Health Kit");
        System.out.println("6. Move to near division");
        System.out.println("7. Finish Mission");
        System.out.println("8. Building Info");
        System.out.println("9. Best Path to Target");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    // Handle the game menu choice
    private boolean handleInGameMenuChoice(int choice) {
        switch (choice) {

            case 1:
                game.attackEnemiesInCurrentDivision();
                return true;
            case 2:
                game.collectTarget();
                return true;
            case 3:
                game.collectItems();
                return true;
            case 4:
                game.displayToCruzDetails();
                return true;
            case 5:
                game.getToCruz().useHealthKit();
                return true;
            case 6:
                game.moveToNearbyDivision();
                return true;
            case 7:
                game.finalizeMission();
                return true;
            case 8:
                game.displayBuildingDetails();
                return true;
            case 9:
                game.displayBestPathToTarget();
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
}

