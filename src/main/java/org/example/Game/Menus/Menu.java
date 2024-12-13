package org.example.Game.Menus;

import org.example.Game.Entities.ENUMS.DifficultyType;
import org.example.Game.Entities.Game;

import java.io.IOException;
import java.util.Scanner;

/**
 * The Menu class handles the user interface for the game, presenting the main menu,
 * the game selection, difficulty selection, and in-game actions. It also handles
 * loading the mission file and initiating the game logic.
 */
public class Menu {

    /**
     * Scanner object for reading user input from the console.
     */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * The Game instance that handles the logic and state of the game.
     */
    private Game game;

    /**
     * The file path for the mission file to be loaded into the game.
     */
    private final String missionFile;

    /**
     * Flag that indicates if it is the first fight of the game.
     * This is used to display specific content during the initial fight.
     */
    private boolean initialFight = true;


    /**
     * Constructor for the Menu class.
     * Initializes the mission file which will be loaded later.
     *
     * @param missionFile The mission file to be loaded.
     */
    public Menu(String missionFile) {
        this.missionFile = missionFile;
    }

    /**
     * Displays the main menu and processes the user's input.
     */
    public void displayMenu() {
        while (true) {
            printMainMenu();
            int choice = getValidMenuChoice();
            handleMainMenuChoice(choice);
        }
    }

    /**
     * Prints the main menu with available game options.
     */
    private void printMainMenu() {
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("                       MENU                         ");
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("1. Start Manual Game");
        System.out.println("2. Start Automatic Game");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    /**
     * Prompts the user for a valid menu option and returns it.
     *
     * @return The user's choice.
     */
    private int getValidMenuChoice() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                System.out.println("You entered: " + input);

                int choice = Integer.parseInt(input);

                if (choice == 1 || choice == 2 || choice == 3 || choice == 4 || choice == 0) {
                    return choice;
                } else {
                    System.out.println("Invalid choice! Please select a valid option.");
                }
            } catch (NumberFormatException e) {

                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    /**
     * Processes the user's choice from the main menu.
     *
     * @param choice The user's selected option.
     */
    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1:
                startManualGame();
                return;
            case 2:
                startAutomaticGame();
                return;
            case 0:
                System.out.println("Exiting...");
                System.exit(0);
                return;
            default:
                System.out.println("Invalid choice! Please select a valid option.");
        }
    }

    /**
     * Displays the mission records.
     */
    private void displayMissionRecords() {
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("                   MISSION RECORDS                  ");
        System.out.println("════════════════════════════════════════════════════");

        game.displayMissionRecordsDetails();
    }

    /**
     * Starts the manual game and shows the game menu.
     */
    private void startManualGame() {
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("               STARTING MANUAL GAME                 ");
        System.out.println("════════════════════════════════════════════════════");

        loadMissionAndShowDetails();
        showDifficultySelection();
    }

    /**
     * Starts the automatic game and shows the game menu.
     */
    private void startAutomaticGame() {
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("               STARTING AUTOMATIC GAME              ");
        System.out.println("════════════════════════════════════════════════════");

        loadMissionAndShowDetails();
        game.displayBestAutomaticPathToTargetAndReturn();

    }

    /**
     * Loads the mission and displays the mission details.
     */
    private void loadMissionAndShowDetails() {
        try {
            game = new Game(this.missionFile, this.scanner);
            game.loadMissionFromJson();
            game.loadMissionRecordsFromJson();
            game.displayMissionInfo();
        } catch (IOException e) {
            System.err.println("Error loading mission: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays the difficulty selection menu.
     */
    private void showDifficultySelection() {
        DifficultyType difficulty = getDifficultyFromUser();
        createGameWithDifficulty(difficulty);
    }

    /**
     * Prompts the user to select a difficulty level.
     *
     * @return The selected difficulty type.
     */
    private DifficultyType getDifficultyFromUser() {
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("                    SELECT DIFFICULTY               ");
        System.out.println("════════════════════════════════════════════════════");

        int difficultyInput = getValidDifficultyInput();
        return mapInputToDifficulty(difficultyInput);
    }

    /**
     * Prompts the user for a valid difficulty input.
     *
     * @return The difficulty level (1, 2, or 3).
     */
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

    /**
     * Maps the numerical input to the corresponding difficulty type.
     *
     * @param input The difficulty level number (1, 2, or 3).
     * @return The corresponding difficulty type.
     */
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

    /**
     * Creates the game with the selected difficulty level.
     *
     * @param difficulty The selected difficulty.
     */
    private void createGameWithDifficulty(DifficultyType difficulty) {
        System.out.println("Loading game with difficulty: " + difficulty);
        try {
            game.createToCruzCharacter(difficulty);
            displayInGameMenu();
        } catch (IOException e) {
            System.err.println("Error creating ToCruz character: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays the in-game menu with available options.
     */
    private void displayInGameMenu() {
        boolean running = true;
        while (running) {
            printGameMenu();
            int choice = getValidMenuChoice();
            running = handleGameMenuChoice(choice);
        }
    }

    /**
     * Prints the game menu.
     */
    private void printGameMenu() {
        try {
            game.deleteLogsFile();
        } catch (IOException e) {
            System.out.println("Error deleting file");
        }
        game.displayMissionInfo();
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("                      GAME MENU                     ");
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("1. Start the Game");
        System.out.println("2. View ToCruz Info");
        System.out.println("3. View Mission Info and Building Layout");
        System.out.println("4. View Missions Records");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    /**
     * Processes the user's choice from the in-game menu.
     *
     * @param choice The option chosen by the user.
     * @return Returns a boolean indicating whether the menu should continue.
     */
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
            case 4:
                displayMissionRecords();
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

    /**
     * Starts the game and processes in-game actions.
     */
    private int getValidInGameMenuChoice() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                System.out.println("You entered: " + input);

                int choice = Integer.parseInt(input);

                if (choice == 1 || choice == 2 || choice == 3 || choice == 4 || choice == 5 || choice == 6 || choice == 7 || choice == 8 || choice == 9 || choice == 10 || choice == 0) {
                    return choice;
                } else {
                    System.out.println("Invalid choice! Please select a valid option.");
                }
            } catch (NumberFormatException e) {

                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    /**
     * Prints the in-game action menu with available options.
     */
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

    /**
     * Prints the in-game menu.
     */
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
        System.out.println("10. Best Path to Near Life Item");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    /**
     * Processes the user's choice from the in-game action menu.
     *
     * @param choice The option chosen by the user.
     * @return Returns a boolean indicating whether the menu should continue.
     */
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
                if (game.finalizeMission()) {
                    System.exit(0);
                }
                return true;
            case 8:
                game.displayBuildingDetails();
                return true;
            case 9:
                game.displayBestPathToTarget();
                return true;
            case 10:
                game.displayBestPathToNearLifeItem();
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

