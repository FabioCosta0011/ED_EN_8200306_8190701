package org.example.Game.Entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Game.Entities.ENUMS.DifficultyType;
import org.example.Game.Entities.ENUMS.ItemType;
import org.example.Game.Entities.Interfaces.*;
import org.example.Structures.Implementations.ArrayUnorderedList;
import org.example.Structures.Interfaces.UnorderedListADT;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Game {

    private final String missionPath;
    private Mission mission;
    private IToCruz toCruz;
    private Scanner scanner;
    private DifficultyType difficulty;
    private BuildingGraph<IDivision> buildingGraph;

    public Game(String missionPath, Scanner scanner) {
        this.missionPath = missionPath;
        this.mission = new Mission();
        this.toCruz = null;
        this.scanner = scanner;
        this.difficulty = null;
        this.buildingGraph = new BuildingGraph<>();
    }

    public Mission getMission() {
        return mission;
    }

    public void setToCruz(IToCruz toCruz) {
        this.toCruz = toCruz;
    }

    public IToCruz getToCruz() {
        return toCruz;
    }

    public DifficultyType getDifficultyType() {
        return difficulty;
    }

    public void setDifficultyType(DifficultyType difficultyType) {
        this.difficulty = difficultyType;
    }

    // Load mission from the JSON file
    public Mission loadMissionFromJson() throws IOException {
        // Load the JSON file from resources
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(missionPath);

        if (inputStream == null) {
            throw new IOException("File not found: " + missionPath);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(inputStream);

        // Parse mission details
        String missionCode = root.get("cod-missao").asText();
        mission.setMissionCode(missionCode);
        int version = root.get("versao").asInt();
        mission.setMissionVersion(version);

        // Parse target
        JsonNode targetNode = root.get("alvo");
        String targetDivision = targetNode.get("divisao").asText();
        // Map the target type manually to match enum
        String targetType = targetNode.get("tipo").asText();  // Mapping string to enum value
        ITarget target = new Target(new Division(targetDivision), targetType);
        mission.setMissionTarget(target);


        // Parse divisions
        JsonNode building = root.get("edificio");
        for (JsonNode divisionNode : building) {
            String divisionName = divisionNode.asText();
            Division division = new Division(divisionName);
            mission.getDivisions().addVertex(division);
            buildingGraph.addVertex(division);
        }


        // Parse connections
        JsonNode connections = root.get("ligacoes");
        for (JsonNode connection : connections) {
            String from = connection.get(0).asText();
            String to = connection.get(1).asText();
            IDivision fromDivision = new Division(from);
            IDivision toDivision = new Division(to);
            mission.getDivisions().addEdge(fromDivision, toDivision);
            buildingGraph.addEdge(fromDivision, toDivision);
        }

        // Parse enemies
        JsonNode enemies = root.get("inimigos");
        for (JsonNode enemyNode : enemies) {
            String name = enemyNode.get("nome").asText();
            int power = enemyNode.get("poder").asInt();
            String division = enemyNode.get("divisao").asText();
            IEnemy enemy = new Enemy(name, power, new Division(division));
            mission.getEnemies().addToRear(enemy);
        }

        // Parse entry points
        JsonNode entryPoints = root.get("entradas-saidas");
        for (JsonNode entryPoint : entryPoints) {
            String divisionName = entryPoint.asText();
            IDivision entryDivision = new Division(divisionName);
            mission.getEntryPoints().addToRear(entryDivision);
        }

        // Parse items
        JsonNode items = root.get("itens");
        for (JsonNode itemNode : items) {
            String divisionName = itemNode.get("divisao").asText();
            String type = itemNode.get("tipo").asText();
            IDivision division = new Division(divisionName);

            if (type.equals("kit de vida")) {
                int recoveryPoints = itemNode.get("pontos-recuperados").asInt();
                IItem item = Item.createLifeKit(division, recoveryPoints);
                mission.getItems().addToRear(item);
            } else if (type.equals("colete")) {
                int extraPoints = itemNode.get("pontos-extra").asInt();
                IItem item = Item.createBulletProofVest(division, extraPoints);
                mission.getItems().addToRear(item);
            }
        }

        return mission;
    }

    public void createToCruzCharacter(DifficultyType difficulty) throws IOException {
        int power = difficulty.getPower();
        int maxHealthKits = difficulty.getMaxHealthKits();

        ArrayUnorderedList<IDivision> entryPoints = (ArrayUnorderedList<IDivision>) mission.getEntryPoints();
        if (!entryPoints.isEmpty()) {
            System.out.println("Select Entry Point for ToCruz:");
            for (int i = 0; i < entryPoints.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, entryPoints.getElement(i).getName());
            }

            int entryPointChoice = -1;
            boolean validChoice = false;

            while (!validChoice) {
                System.out.print("Choose an entry point (1 to " + entryPoints.size() + "): ");
                try {
                    entryPointChoice = Integer.parseInt(scanner.nextLine()) - 1;

                    if (entryPointChoice >= 0 && entryPointChoice < entryPoints.size()) {
                        validChoice = true;
                    } else {
                        System.out.println("Invalid choice! Please select a valid entry point.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a number.");
                }
            }

            IDivision startingDivision = entryPoints.getElement(entryPointChoice);
            toCruz = new ToCruz(power,null, startingDivision);
            toCruz.setCurrentDivision(startingDivision);
            toCruz.setMaxHealthKits(maxHealthKits);
            this.difficulty = difficulty;

            setToCruz(toCruz);
        }
    }

    public void displayMissionInfo() {
        // Printing mission general information
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("                      MISSION INFO                  ");
        System.out.println("════════════════════════════════════════════════════");
        System.out.printf("Mission Code : %s%n", mission.getMissionCode());
        System.out.printf("Version      : %d%n", mission.getVersion());
        System.out.printf("Target Type  : %s%n", mission.getTarget().getType());
        System.out.printf("Target Division : %s%n", mission.getTarget().getDivision().getName());
        System.out.println();
    }

    public void displayBuildingDetails() {
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("                   BUILDING LAYOUT                  ");
        System.out.println("════════════════════════════════════════════════════");
        ArrayUnorderedList<IDivision> divisions = mission.getDivisions().getAllVertices();

        for (int i = 0; i < divisions.size(); i++) {
            IDivision division = divisions.getElement(i);

            System.out.printf("  ▌ Div %d: %s%n", (i + 1), division.getName());
            System.out.println("  ────────────────────────────────────────────────");

            // Reusing auxiliary methods
            displayConnections(division);
            displayItems(division);
            displayEnemies(division);
            displayTarget(division);
            displayToCruzPresence(division);
            displayEntryExitPoint(division);

            System.out.println();
        }
        System.out.println("════════════════════════════════════════════════════");
    }

    /**
     * Displays detailed information about the current division where To Cruz is located.
     */
    public void displayCurrentDivisionDetails() {
        IDivision currentDivision = toCruz.getCurrentDivision();
        System.out.println("               CURRENT DIVISION DETAILS             ");
        System.out.println("  ────────────────────────────────────────────────");
        System.out.printf("  ▌ Division: %s%n", currentDivision.getName());
        System.out.println("  ────────────────────────────────────────────────");

        displayConnections(currentDivision);
        displayItems(currentDivision);
        displayEnemies(currentDivision);
        displayTarget(currentDivision);
        displayToCruzPresence(currentDivision);
        displayEntryExitPoint(currentDivision);

        System.out.println("════════════════════════════════════════════════════");
    }

    /**
     * Displays detailed information about a list of divisions.
     *
     * @param divisions         The list of divisions to display.
     */
    public void displayNearbyDivisionsDetails(ArrayUnorderedList<IDivision> divisions) {
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("                   NEARBY DIVISIONS                 ");
        System.out.println("════════════════════════════════════════════════════");
        for (int i = 0; i < divisions.size(); i++) {
            IDivision division = divisions.getElement(i);
            System.out.printf("  ▌ Option (%d): %s%n", (i + 1), division.getName());
            System.out.println("  ────────────────────────────────────────────────");

            displayConnections(division);
            displayItems(division);
            displayEnemies(division);
            displayTarget(division);
            displayToCruzPresence(division);
            displayEntryExitPoint(division);

            System.out.println();
        }
        System.out.println("════════════════════════════════════════════════════");
    }

    private void displayConnections(IDivision division) {
        ArrayUnorderedList<IDivision> neighbors = mission.getDivisions().getNeighbors(division);
        if (!neighbors.isEmpty()) {
            System.out.println("    Connections:");
            for (int j = 0; j < neighbors.size(); j++) {
                IDivision neighbor = neighbors.getElement(j);
                System.out.printf("      └─> %s%n", neighbor.getName());
            }
        } else {
            System.out.println("    No connections available.");
        }
    }

    private void displayItems(IDivision division) {
        System.out.println("    Items in this division:");
        UnorderedListADT<IItem> itemsInDivision = mission.getItemsByDivision(division);
        if (!itemsInDivision.isEmpty()) {
            for (IItem item : itemsInDivision) {
                System.out.printf("      Item: %s%n", item.getType());
                if (item.getType() == ItemType.LIFE_KIT) {
                    System.out.printf("        Recovery Points: %d%n", item.getRecoveryPoints());
                } else if (item.getType() == ItemType.BULLET_PROOF_VEST) {
                    System.out.printf("        Extra Points: %d%n", item.getExtraPoints());
                }
            }
        } else {
            System.out.println("      No items in this division.");
        }
    }

    private void displayEnemies(IDivision division) {
        System.out.println("    Enemies in this division:");
        UnorderedListADT<IEnemy> enemiesInDivision = mission.getEnemiesByDivision(division);
        if (!enemiesInDivision.isEmpty()) {
            for (IEnemy enemy : enemiesInDivision) {
                System.out.printf("    🤖 Enemy: %s%n", enemy.getName());
                System.out.printf("        Power: %d%n", enemy.getPower());
            }
        } else {
            System.out.println("      No enemies in this division.");
        }
    }

    private void displayTarget(IDivision division) {
        ITarget target = mission.getTarget();
        if (target != null && target.getDivision().equals(division) && !target.isTaken()) {
            System.out.printf("    🚩 Target is present in this division: %s%n", target.getType());
        }
    }

    private void displayEntryExitPoint(IDivision division) {
        if (mission.getEntryPoints().contains(division)) {
            System.out.println("    🔄 Entry/Exit Point");
        }
    }

    private void displayToCruzPresence(IDivision division) {
        if (toCruz.getCurrentDivision().equals(division)) {
            if (toCruz.isCarryingTarget()) {
                System.out.println("    🚶 To Cruz is here and is carrying the 🚩 target!");
            } else {
                System.out.println("    🚶 To Cruz is here.");
            }
        }
    }

    public void displayToCruzDetails() {
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("                    TO CRUZ INFO                    ");
        System.out.println("════════════════════════════════════════════════════");
        System.out.printf("Name            : %s%n", toCruz.getName());
        System.out.printf("Power           : %d%n", toCruz.getPower());
        System.out.printf("Health          : %d%n", toCruz.getHealth());
        System.out.println("Bullet Proof Vest: " + (toCruz.isUsingBulletProofVest() ? "Yes" : "No"));
        System.out.printf("Difficulty      : %s%n", getDifficultyType());
        System.out.printf("Max Health Kits : %d%n", toCruz.getMaxHealthKits());
        System.out.println("Number of Health Kits: " + toCruz.getHealthKits().size());
        System.out.printf("Starting Division: %s%n", toCruz.getCurrentDivision().getName());
    }

    public void collectTarget() {
        IDivision currentDivision = toCruz.getCurrentDivision();
        ITarget target = mission.getTarget();
        UnorderedListADT<IEnemy> enemiesInDivision = mission.getEnemiesByDivision(currentDivision);

        if (!enemiesInDivision.isEmpty()) {
            System.out.println("Cannot capture the target. Enemies are present in this division.");
            return;
        }
        if (target != null && target.getDivision().equals(currentDivision)) {
            toCruz.setTarget(target);
            mission.getTarget().takeTarget();
            System.out.println("Target captured successfully!");
        } else {
            System.out.println("No target available in this division.");
        }
    }

    public void collectItems() {
        IDivision currentDivision = toCruz.getCurrentDivision();
        UnorderedListADT<IItem> itemsInDivision = mission.getItemsByDivision(currentDivision);

        for (IItem item : itemsInDivision) {
            if (item.getType() == ItemType.LIFE_KIT) {
                toCruz.addHealthKit(item);
                mission.getItems().remove(item);
            } else if (item.getType() == ItemType.BULLET_PROOF_VEST) {
                toCruz.consumeBulletProofVest(item);
                mission.getItems().remove(item);
            }
        }
        if (itemsInDivision.isEmpty()) {
            System.out.println("No items available to pick up in this division.");
        }
    }

    public void attackEnemiesInCurrentDivision() {
        IDivision currentDivision = toCruz.getCurrentDivision();
        UnorderedListADT<IEnemy> enemiesInDivision = mission.getEnemiesByDivision(currentDivision);

        if (!enemiesInDivision.isEmpty()) {
            toCruz.attackEnemies(enemiesInDivision, mission);
            attackToCruz();

            UnorderedListADT<IEnemy> allEnemiesOutsideCurrentDivision = mission.getAllEnemiesOutsideCurrentDivision(currentDivision, toCruz);
            for (IEnemy enemy : allEnemiesOutsideCurrentDivision) {
                moveEnemyToRandomNearbyDivision(enemy, currentDivision);
            }

            System.out.println("To Cruz attacked enemies in the current division!");
        } else {
            System.out.println("No enemies available in this division to attack!");
        }
    }

    public void attackToCruz() {
        IDivision currentDivision = toCruz.getCurrentDivision();
        UnorderedListADT<IEnemy> enemiesInDivision = mission.getEnemiesByDivision(currentDivision);

            if (!enemiesInDivision.isEmpty()) {
                for (IEnemy enemy : enemiesInDivision) {
                    enemy.attackToCruz(toCruz);
                }

                System.out.println("ToCruz attacked by enemies!");
            } else {
                System.out.println("No enemies available in this division, to attack To Cruz!");
            }
    }

    public void moveToNearbyDivision() {
        IDivision currentDivision = toCruz.getCurrentDivision();
        ArrayUnorderedList<IDivision> nearbyDivisions = buildingGraph.getNeighbors(currentDivision);

        if (nearbyDivisions.isEmpty()) {
            System.out.println("No nearby divisions to move to.");
            return;
        }

        displayNearbyDivisionsDetails(nearbyDivisions);

        System.out.print("Choose a division to move to: ");
        int choice = getValidDivisionChoice(nearbyDivisions.size());

        IDivision newDivision = nearbyDivisions.getElement(choice - 1);

        if (moveToNewDivision(newDivision)) {
            System.out.println("You have successfully moved to the new division: " + newDivision.getName());
        } else {
            System.out.println("Failed to move to the new division.");
        }
    }

    private void moveEnemyToRandomNearbyDivision(IEnemy enemy, IDivision toCruzCurrentDivision) {
        IDivision currentDivision = enemy.getCurrentDivision();
        ArrayUnorderedList<IDivision> nearbyDivisions = buildingGraph.getNeighbors(currentDivision);

        if (!nearbyDivisions.isEmpty()) {
            int randomIndex = (int) (Math.random() * nearbyDivisions.size());
            IDivision newDivision = nearbyDivisions.getElement(randomIndex);

            enemy.setCurrentDivision(newDivision);
            System.out.println("════════════════════════════════════════════════════");
            System.out.printf("Enemy '%s' moved from '%s' to '%s'.%n",
                    enemy.getName(), currentDivision.getName(), newDivision.getName());
            System.out.println("════════════════════════════════════════════════════");

            if (newDivision.equals(toCruzCurrentDivision)) {
                enemy.attackToCruz(toCruz);
                System.out.println("═══════════════════════════════════════════════");
                System.out.println("ToCruz attacked by new enemy in this division!");
                System.out.println("═══════════════════════════════════════════════");
            }
        } else {
            System.out.printf("Enemy '%s' remains in '%s' as there are no nearby divisions.%n",
                    enemy.getName(), currentDivision.getName());
        }
    }

    public boolean moveToNewDivision(IDivision newDivision) {
        if (hasEnemiesInCurrentDivision()) {
            System.out.println("There are enemies in the current division. You cannot move yet.");
            return false;
        }

        toCruz.setCurrentDivision(newDivision);
        System.out.println("You moved to the new division: " + newDivision.getName());

        printInGameDivisionFight();
        return true;
    }

    private int getValidDivisionChoice(int size) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);
                if (choice > 0 && choice <= size) {
                    return choice;
                } else {
                    System.out.println("Invalid choice! Please select a valid division.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    public boolean hasEnemiesInCurrentDivision() {
        IDivision currentDivision = toCruz.getCurrentDivision();
        UnorderedListADT<IEnemy> enemiesInDivision = mission.getEnemiesByDivision(currentDivision);

        return !enemiesInDivision.isEmpty();
    }

    public void printInGameDivisionFight() {
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("           IN-GAME Current Division Fight           ");
        System.out.println("════════════════════════════════════════════════════");

        attackEnemiesInCurrentDivision();

        System.out.println("════════════════════════════════════════════════════");
    }

    public void finalizeMission() {
        if (toCruz == null) {
            System.out.println("ToCruz is not initialized.");
            return;
        }

        if (toCruz.getTarget() == null) {
            System.out.println("Mission cannot be finalized. ToCruz has not captured the target.");
            return;
        }

        ArrayUnorderedList<IDivision> entryPoints = (ArrayUnorderedList<IDivision>) mission.getEntryPoints();
        IDivision currentDivision = toCruz.getCurrentDivision();

        if (!entryPoints.contains(currentDivision)) {
            System.out.println("Mission cannot be finalized. ToCruz is not in an entry point.");
            return;
        }

        System.out.println("Mission finalized successfully!");
    }

    public boolean toCruzDies() {
        if (toCruz.getHealth() <= 0) {
            System.out.println("ToCruz has died! Game Over!");
            return true;
        }
        return false;
    }


}