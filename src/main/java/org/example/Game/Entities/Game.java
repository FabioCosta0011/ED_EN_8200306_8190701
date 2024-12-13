package org.example.Game.Entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.Game.Entities.ENUMS.DifficultyType;
import org.example.Game.Entities.ENUMS.ItemType;
import org.example.Game.Entities.Interfaces.*;
import org.example.Structures.Implementations.ArrayUnorderedList;
import org.example.Structures.Implementations.LinkedQueue;
import org.example.Structures.Implementations.LinkedStack;
import org.example.Structures.Interfaces.OrderedListADT;
import org.example.Structures.Interfaces.QueueADT;
import org.example.Structures.Interfaces.StackADT;
import org.example.Structures.Interfaces.UnorderedListADT;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class Game {

    private final String missionPath;
    private final String missionRecordsPath;
    private Mission mission;
    private IToCruz toCruz;
    private Scanner scanner;
    private DifficultyType difficulty;
    private BuildingGraph<IDivision> buildingGraph;

    public Game(String missionPath, Scanner scanner) {
        this.missionPath = missionPath;
        this.missionRecordsPath = "missionsRecords.json";
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

    public Mission loadMissionFromJson() throws IOException {
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
        String targetType = targetNode.get("tipo").asText();
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

    public OrderedListADT<IRecord> loadMissionRecordsFromJson() throws IOException {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(missionRecordsPath);

        if (inputStream == null) {
            throw new IOException("JSON file not found: " + missionRecordsPath);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(inputStream);

        JsonNode missionsNode = rootNode.path("missoes");

        if (!missionsNode.isArray()) {
            throw new IOException("Invalid format: The node 'missoes' is not an array.");
        }

        String currentMissionCode = mission.getMissionCode();
        int currentVersion = mission.getVersion();

        boolean missionFound = false;

        for (JsonNode missionNode : missionsNode) {
            String missionCode = missionNode.path("cod-missao").asText();
            int version = missionNode.path("versao").asInt();

            if (!missionCode.equals(currentMissionCode) || version != currentVersion) {
                continue;
            }

            missionFound = true;

            JsonNode recordsNode = missionNode.path("recordes");

            if (!recordsNode.isArray()) {
                throw new IOException("Invalid format: The node 'recordes' is not an array.");
            }

            for (JsonNode recordNode : recordsNode) {
                String dateStr = recordNode.path("data").asText();
                int healthPoints = recordNode.path("pontosDeVida").asInt();

                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);

                    Record record = new Record(date, healthPoints);
                    mission.getRecords().add(record);
                } catch (ParseException e) {
                    throw new IOException("Erro ao converter data: " + dateStr, e);
                }
            }
        }

        if (!missionFound) {
            System.out.println("No matching mission found for code: " + currentMissionCode + " and version: " + currentVersion);
        }

        return mission.getRecords();
    }

    public void saveMissionRecordsToJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        File file = new File("src/main/resources/" + missionRecordsPath);

        ObjectNode rootNode;
        if (file.exists()) {
            rootNode = (ObjectNode) objectMapper.readTree(file);
        } else {
            rootNode = objectMapper.createObjectNode();
        }

        ArrayNode missionsNode = rootNode.has("missoes") ? (ArrayNode) rootNode.get("missoes") : objectMapper.createArrayNode();

        for (int i = 0; i < missionsNode.size(); i++) {
            ObjectNode existingMission = (ObjectNode) missionsNode.get(i);
            String existingCode = existingMission.get("cod-missao").asText();
            int existingVersion = existingMission.get("versao").asInt();

            if (existingCode.equals(mission.getMissionCode()) && existingVersion == mission.getVersion()) {
                missionsNode.remove(i);
                break;
            }
        }

        ObjectNode missionNode = objectMapper.createObjectNode();
        missionNode.put("cod-missao", mission.getMissionCode());
        missionNode.put("versao", mission.getVersion());

        ArrayNode recordsNode = objectMapper.createArrayNode();
        for (IRecord record : mission.getRecords()) {
            ObjectNode recordNode = objectMapper.createObjectNode();
            recordNode.put("data", new SimpleDateFormat("yyyy-MM-dd").format(record.getDate())); // Formata a data corretamente
            recordNode.put("pontosDeVida", record.getHealthPoints());
            recordsNode.add(recordNode);
        }

        missionNode.set("recordes", recordsNode);

        missionsNode.add(missionNode);

        rootNode.set("missoes", missionsNode);

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);

        System.out.println("Mission records saved to JSON!");
    }

    public void createToCruzCharacter(DifficultyType difficulty) throws IOException {
        int power = difficulty.getPower();
        int maxHealthKits = difficulty.getMaxHealthKits();

        ArrayUnorderedList<IDivision> entryPoints = (ArrayUnorderedList<IDivision>) mission.getEntryPoints();
        if (!entryPoints.isEmpty()) {
            displayEntryPoints(entryPoints);
            System.out.println("Select Entry Point for To Cruz:");
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
            toCruz = new ToCruz(power, null, startingDivision);
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

    public void displayMissionRecordsDetails() {

        System.out.println(mission.getMissionCode() + " - " + mission.getVersion());
        int count = mission.getRecords().size();
        System.out.println("Current Records:");
        Iterator<IRecord> iterator = mission.getRecords().iterator();
        while (iterator.hasNext()) {
            IRecord record = iterator.next();
            System.out.print(count + "º ");
            displayMissionRecords(record);
            count--;
        }
    }

    /**
     * Displays detailed information about a list of divisions.
     *
     * @param divisions The list of divisions to display.
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

    public void displayEntryPoints(ArrayUnorderedList<IDivision> divisions) {
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("                   ENTRY/EXIT POINTS                ");
        System.out.println("════════════════════════════════════════════════════");
        for (int i = 0; i < divisions.size(); i++) {
            IDivision division = divisions.getElement(i);
            System.out.printf("  ▌ Entry/Exit Point (%d): %s%n", (i + 1), division.getName());
            System.out.println("  ────────────────────────────────────────────────");

            displayConnections(division);
            displayItems(division);
            displayEnemies(division);
            displayTarget(division);
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

    private void displayMissionRecords(IRecord record) {

        System.out.printf("Date: %s, Health Points: %d\n",
                new SimpleDateFormat("yyyy-MM-dd").format(record.getDate()),
                record.getHealthPoints());

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
                System.out.println("    🕵️ To Cruz is here and is carrying the 🚩 target!");
            } else {
                System.out.println("    🕵️ To Cruz is here.");
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
        System.out.printf("Current Division: %s%n", toCruz.getCurrentDivision().getName());
        if(toCruz.isCarryingTarget()) {
            System.out.println("Has the target! 🚩");
        }
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

    public boolean finalizeMission() {
        if (toCruz == null) {
            System.out.println("ToCruz is not initialized.");
            return false;
        }

        if (toCruz.getTarget() == null) {
            System.out.println("Mission cannot be finalized. ToCruz has not captured the target.");
            return false;
        }

        ArrayUnorderedList<IDivision> entryPoints = (ArrayUnorderedList<IDivision>) mission.getEntryPoints();
        IDivision currentDivision = toCruz.getCurrentDivision();

        if (!entryPoints.contains(currentDivision)) {
            System.out.println("Mission cannot be finalized. ToCruz is not in an entry point.");
            return false;
        }

        int healthRecord = toCruz.getHealth();
        mission.getRecords().add(new Record(new Date(), healthRecord));

        try {
            saveMissionRecordsToJson();
        } catch (IOException e) {
            System.out.println("Error saving mission records: " + e.getMessage());
        }

        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                       🎯 Mission Status: Success! 🚀                          ");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.println("\nThe mission is successful! Great job agent!!");
        System.out.println("To Cruz have finished with a total of " + toCruz.getHealth() + " health points!");
        System.out.println("\n══════════════════════════════════════════════════════════════════════════════");
        return true;
    }

    public boolean toCruzDies() {
        if (toCruz.getHealth() <= 0) {
            System.out.println("════════════════════════════════════════════════════════════════════════════════");
            System.out.println("                    ☠️  GAME OVER: TO CRUZ HAS FALLEN! ☠️                       ");
            System.out.println("════════════════════════════════════════════════════════════════════════════════");
            System.out.println("The brave hero, To Cruz, has succumbed to his injuries.");
            System.out.println("His health has dropped to zero. The mission ends here.");
            System.out.println("To Cruz ended the mission with " + toCruz.getHealth() + " health points.");
            System.out.println("════════════════════════════════════════════════════════════════════════════════");
            return true;
        }
        return false;
    }

    public void displayBestPathToTarget() {
        QueueADT<IDivision> bestPath = mission.findBestPath(
                toCruz.getCurrentDivision(), mission.getTarget().getDivision()
        );

        if (!bestPath.isEmpty()) {
            System.out.println("════════════════════════════════════════════════════");
            System.out.println("                 BEST PATH TO TARGET                ");
            System.out.println("════════════════════════════════════════════════════");

            StackADT<IDivision> pathCloneForDisplay = new LinkedStack<>();
            StackADT<IDivision> pathCloneForVerification = new LinkedStack<>();

            while (!bestPath.isEmpty()) {
                IDivision division = bestPath.dequeue();
                pathCloneForDisplay.push(division);
                pathCloneForVerification.push(division);
            }

            IDivision previous = null;
            while (!pathCloneForDisplay.isEmpty()) {
                IDivision division = pathCloneForDisplay.pop();
                if (previous != null) {
                    System.out.print("└─> ");
                }
                System.out.println(division.getName());
                previous = division;
            }
        }
    }

    public void displayBestPathToNearLifeItem() {
        new ArrayUnorderedList<>();
        UnorderedListADT<IDivision> bestPath = mission.findBestPathToLifeKit(toCruz.getCurrentDivision());

        if (!bestPath.isEmpty()) {
            System.out.println("════════════════════════════════════════════════════");
            System.out.println("              BEST PATH TO NEAR LIFE ITEM           ");
            System.out.println("════════════════════════════════════════════════════");

            IDivision previous = null;
            for (IDivision division : bestPath) {
                if (previous != null) {
                    System.out.print("└─> ");
                }
                System.out.println(division.getName());
                previous = division;
            }
        } else {
            System.out.println("Cant find an path to near life item.");
        }
    }

    public void displayBestAutomaticPathToTargetAndReturn() {

        QueueADT<IDivision> bestPath = mission.findBestRouteFromMultipleEntryPoints(mission.getEntryPoints(), mission.getTarget().getDivision());

        if (!bestPath.isEmpty()) {
            System.out.println("════════════════════════════════════════════════════");
            System.out.println("                 BEST PATH TO TARGET                ");
            System.out.println("════════════════════════════════════════════════════");

            QueueADT<IDivision> pathCloneForDisplay = new LinkedQueue<>();
            QueueADT<IDivision> pathCloneForVerification = new LinkedQueue<>();

            while (!bestPath.isEmpty()) {
                IDivision division = bestPath.dequeue();
                pathCloneForDisplay.enqueue(division);
                pathCloneForVerification.enqueue(division);
            }

            IDivision previous = null;
            while (!pathCloneForDisplay.isEmpty()) {
                IDivision division = pathCloneForDisplay.dequeue();
                if (previous != null) {
                    System.out.print("└─> ");
                }
                System.out.println(division.getName());
                previous = division;
            }


            System.out.println("\nChecking if it's possible to return to the entry point...");
            QueueADT<IDivision> verifiedPath = mission.verifyPathToEntry(pathCloneForVerification, mission.getPoints());

            if (verifiedPath != null && !verifiedPath.isEmpty()) {
                System.out.println("════════════════════════════════════════════════════");
                System.out.println("      PATH VERIFIED: RETURN TO ENTRY POINT          ");
                System.out.println("════════════════════════════════════════════════════");

                StackADT<IDivision> reversedReturnPath = new LinkedStack<>();
                while (!verifiedPath.isEmpty()) {
                    reversedReturnPath.push(verifiedPath.dequeue());
                }

                previous = null;
                while (!reversedReturnPath.isEmpty()) {
                    IDivision division = reversedReturnPath.pop();
                    if (previous != null) {
                        System.out.print("└─> ");
                    }
                    System.out.println(division.getName());
                    previous = division;
                }
                System.out.println("════════════════════════════════════════════════════════════════════════════════");
                System.out.println("                        🎯 Mission Status: Success! 🚀                          ");
                System.out.println("════════════════════════════════════════════════════════════════════════════════");
                System.out.println("\nThe mission is successful! According to the automatic simulation,");
                System.out.println("To Cruz will finish with a total of " + mission.getPoints() + " health points!");
                System.out.println("\n════════════════════════════════════════════════════════════════════════════════");
            } else {
                System.out.println("It is not possible to find a path back to the entry point.");
            }
        }
    }
}