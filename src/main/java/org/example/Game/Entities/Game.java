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

    private Mission mission;
    private final String missionPath;
    private IToCruz toCruz;
    private Scanner scanner;
    private DifficultyType difficulty;

    public Game(String missionPath, Scanner scanner) {
        this.missionPath = missionPath;
        this.mission = new Mission();
        this.toCruz = null;
        this.scanner = scanner;
        this.difficulty = null;
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
        }


        // Parse connections
        JsonNode connections = root.get("ligacoes");
        for (JsonNode connection : connections) {
            String from = connection.get(0).asText();
            String to = connection.get(1).asText();
            IDivision fromDivision = new Division(from);
            IDivision toDivision = new Division(to);
            mission.getDivisions().addEdge(fromDivision, toDivision);
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
                IItem item = Item.createExtraPoints(division, extraPoints);
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

                    // Verifica se a escolha está dentro do intervalo válido
                    if (entryPointChoice >= 0 && entryPointChoice < entryPoints.size()) {
                        validChoice = true;  // A escolha é válida
                    } else {
                        System.out.println("Invalid choice! Please select a valid entry point.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a number.");
                }
            }

            IDivision startingDivision = entryPoints.getElement(entryPointChoice);
            toCruz = new ToCruz(power, startingDivision);
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
        // Displaying building divisions
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("                   BUILDING LAYOUT                  ");
        System.out.println("════════════════════════════════════════════════════");
        ArrayUnorderedList<IDivision> divisions = mission.getDivisions().getAllVertices();
        for (int i = 0; i < divisions.size(); i++) {
            IDivision division = divisions.getElement(i);

            System.out.printf("  ▌ Div %d: %s%n", (i + 1), division.getName());
            System.out.println("  ────────────────────────────────────────────────");

            // Displaying connections
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

            // Displaying items in this division
            System.out.println("    Items in this division:");
            boolean divisionHasItems = false;
            for (IItem item : mission.getItems()) {
                if (item.getDivision().equals(division)) {
                    System.out.printf("      Item: %s%n", item.getType());

                    // Check item type using the enum
                    if (item.getType() == ItemType.LIFE_KIT) {
                        System.out.printf("        Recovery Points: %d%n", item.getRecoveryPoints());
                    } else if (item.getType() == ItemType.BULLET_PROOF_VEST) {
                        System.out.printf("        Extra Points: %d%n", item.getExtraPoints());
                    }
                    divisionHasItems = true;
                }
            }

            if (!divisionHasItems) {
                System.out.println("      No items in this division.");
            }

            // Displaying enemies in this division
            System.out.println("    Enemies in this division:");
            boolean divisionHasEnemies = false;
            for (IEnemy enemy : mission.getEnemies()) {
                if (enemy.getCurrentDivision().equals(division)) {
                    System.out.printf("      Enemy: %s%n", enemy.getName());
                    System.out.printf("        Power: %d%n", enemy.getPower());
                    divisionHasEnemies = true;
                }
            }

            if (!divisionHasEnemies) {
                System.out.println("      No enemies in this division.");
            }

            System.out.println();
        }

        // End of the mission details display
        System.out.println("════════════════════════════════════════════════════");
    }

    public void displayToCruzDetails() {
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("             TO CRUZ INFO                 ");
        System.out.println("════════════════════════════════════════════════════");
        System.out.printf("Name            : %s%n", toCruz.getName());
        System.out.printf("Power           : %d%n", toCruz.getPower());
        System.out.printf("Health          : %d%n", toCruz.getHealth());
        System.out.printf("Difficulty      : %s%n", getDifficultyType());
        System.out.printf("Max Health Kits : %d%n", toCruz.getMaxHealthKits());
        System.out.printf("Starting Division: %s%n", toCruz.getCurrentDivision().getName());
    }
}
