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
import org.example.Structures.Interfaces.QueueADT;
import org.example.Structures.Interfaces.StackADT;
import org.example.Structures.Interfaces.UnorderedListADT;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/**
 * The Game class represents the core functionality of a game simulation.
 * It manages the mission, ToCruz character, game difficulty, and building graph.
 * It also provides methods to interact with the game environment and perform game-related actions.
 */
public class Game {

    /**
     * The path to the mission data (usually a file location).
     */
    private final String missionPath;

    /**
     * The path to the mission records data (usually a file location).
     */
    private final String missionRecordsPath;

    /**
     * The path to the last mission info (usually a file location).
     */
    private final String lastMissionInfoPath;

    /**
     * The current mission being played in the game.
     */
    private final Mission mission;

    /**
     * The ToCruz object, representing a player or character in the game.
     */
    private IToCruz toCruz;

    /**
     * A scanner to capture user input during the game.
     */
    private final Scanner scanner;

    /**
     * The current difficulty level of the game.
     */
    private DifficultyType difficulty;

    /**
     * The building graph representing the divisions in the game.
     */
    private final Building<IDivision> building;

    /**
     * A queue that stores information about the last mission.
     */
    private final QueueADT<String> lastMissionInfo;


    /**
     * Constructor to initialize the game with the given mission path and scanner.
     *
     * @param missionPath The path to the mission data (usually a file location).
     * @param scanner     A scanner to capture user input during the game.
     */
    public Game(String missionPath, Scanner scanner) {
        this.missionPath = missionPath;
        this.missionRecordsPath = "missionsRecords.json";
        this.lastMissionInfoPath = "lastMissionInfo.json";
        this.mission = new Mission();
        this.toCruz = null;
        this.scanner = scanner;
        this.difficulty = null;
        this.building = new Building<>();
        this.lastMissionInfo = new LinkedQueue<>();
    }

    /**
     * Sets the ToCruz object (player or character) in the game.
     *
     * @param toCruz The ToCruz object to set.
     */
    public void setToCruz(IToCruz toCruz) {
        this.toCruz = toCruz;
    }

    /**
     * Retrieves the current ToCruz object in the game.
     *
     * @return The current ToCruz object.
     */
    public IToCruz getToCruz() {
        return toCruz;
    }

    /**
     * Retrieves the current difficulty level of the game.
     *
     * @return The current DifficultyType.
     */
    public DifficultyType getDifficultyType() {
        return difficulty;
    }


    /**
     * Loads the mission details from a JSON file into the mission object.
     *
     * @throws IOException if there is an issue reading the file or parsing the data.
     */
    public void loadMissionFromJson() throws IOException {
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
            this.building.addVertex(division);
        }


        // Parse connections
        JsonNode connections = root.get("ligacoes");
        for (JsonNode connection : connections) {
            String from = connection.get(0).asText();
            String to = connection.get(1).asText();
            IDivision fromDivision = new Division(from);
            IDivision toDivision = new Division(to);
            mission.getDivisions().addEdge(fromDivision, toDivision);
            this.building.addEdge(fromDivision, toDivision);
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

    }

    /**
     * Loads mission records from a JSON file and updates the mission records.
     *
     * @throws IOException if there is an issue reading the file or parsing the data.
     */
    public void loadMissionRecordsFromJson() throws IOException {

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

    }

    /**
     * Saves the current mission records to a JSON file.
     *
     * @throws IOException if there is an issue saving the file or parsing the data.
     */
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

    /**
     * Saves the logs of the last mission's movements into a JSON file.
     *
     * This method serializes the movement logs provided in the {@code logs} queue
     * and stores them in a JSON file. If the file already exists, it appends the new
     * logs to the existing file. The logs are stored in a JSON object with each
     * movement log as a key-value pair.
     *
     * @param logs The queue of movement logs to be saved.
     * @throws IOException If an I/O error occurs while writing to the file or reading an existing one.
     */
    public void saveLastMissionMovements(QueueADT<String> logs) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        File file = new File("src/main/resources/" + lastMissionInfoPath);


        ObjectNode rootNode;
        if (file.exists()) {
            rootNode = (ObjectNode) objectMapper.readTree(file);
        } else {
            rootNode = objectMapper.createObjectNode();
        }

        ObjectNode logsNode = rootNode.has("lastMissionInfo") ? (ObjectNode) rootNode.get("lastMissionInfo") : objectMapper.createObjectNode();

        while (!logs.isEmpty()) {
            String newLog = logs.dequeue();
            int logNumber = logsNode.size() + 1;

            String logKey = "movement" + logNumber;

            logsNode.put(logKey, newLog);
        }

        rootNode.set("lastMissionInfo", logsNode);

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);

        System.out.println("Last Mission Info saved to JSON!");
    }

    /**
     * Deletes the log file that stores the last mission's movement information.
     *
     * This method attempts to delete the log file from the filesystem. If the file exists,
     * it will be deleted. The method provides feedback on whether the deletion was
     * successful or if the file was not found.
     *
     * @throws IOException If an I/O error occurs during the file deletion process.
     */
    public void deleteLogsFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        File file = new File("src/main/resources/" + lastMissionInfoPath);

        if (file.exists()) {
            boolean isDeleted = file.delete();
            if (isDeleted) {
                System.out.println("Arquivo de logs apagado com sucesso!");
            } else {
                System.out.println("Falha ao apagar o arquivo de logs.");
            }
        } else {
            System.out.println("Arquivo de logs não encontrado.");
        }
    }


    /**
     * Creates the ToCruz character with the specified difficulty, sets the starting division, and initializes the character's health and power.
     *
     * @param difficulty The difficulty level chosen for the mission. It determines the power and the number of health kits.
     * @throws IOException If there is an issue reading or handling the user input.
     */
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

    /**
     * Displays general information about the mission, including mission code, version, and target details.
     */
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

    /**
     * Displays the building layout with details about divisions, connections, items, enemies, targets, and ToCruz presence.
     */
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
     * Displays details of the current division where ToCruz is located.
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
     * Displays the best path from ToCruz's current division to the target's division.
     */
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

    /**
     * Displays the best path from ToCruz's current division to the nearest life item.
     */
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

    /**
     * Displays the best automatic path from multiple entry points to the target and the path to return to the entry point.
     */
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

    /**
     * Displays the details of the current mission's records, including the mission code,
     * version, and a list of mission records ordered by health points.
     */
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
     * Displays the details of nearby divisions, including their connections, items, enemies,
     * target, and whether they serve as entry/exit points.
     *
     * @param divisions List of nearby divisions to display details for.
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

    /**
     * Displays the details of entry/exit points, including their connections, items, enemies,
     * and whether the target or To Cruz is present.
     *
     * @param divisions List of divisions that are entry/exit points to display details for.
     */
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

    /**
     * Displays the connections (neighbors) of a given division.
     *
     * @param division The division whose connections are to be displayed.
     */
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

    /**
     * Displays the items available in a given division, including item type and specific details
     * such as recovery points or extra points.
     *
     * @param division The division whose items are to be displayed.
     */
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

    /**
     * Displays the enemies present in a given division, including their name and power level.
     *
     * @param division The division whose enemies are to be displayed.
     */
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

    /**
     * Displays the details of a mission record, including the date and health points.
     *
     * @param record The mission record to be displayed.
     */
    private void displayMissionRecords(IRecord record) {

        System.out.printf("Date: %s, Health Points: %d\n",
                new SimpleDateFormat("yyyy-MM-dd").format(record.getDate()),
                record.getHealthPoints());

    }

    /**
     * Displays whether a target is present in a given division and, if so, the type of target.
     *
     * @param division The division to check for the target.
     */
    private void displayTarget(IDivision division) {
        ITarget target = mission.getTarget();
        if (target != null && target.getDivision().equals(division) && !target.isTaken()) {
            System.out.printf("    🚩 Target is present in this division: %s%n", target.getType());
        }
    }

    /**
     * Displays if a division serves as an entry/exit point in the mission.
     *
     * @param division The division to check for entry/exit point status.
     */
    private void displayEntryExitPoint(IDivision division) {
        if (mission.getEntryPoints().contains(division)) {
            System.out.println("    🔄 Entry/Exit Point");
        }
    }

    /**
     * Displays information about "To Cruz"'s presence in a division, including whether
     * "To Cruz" is carrying the target.
     *
     * @param division The division to check for "To Cruz"'s presence.
     */
    private void displayToCruzPresence(IDivision division) {
        if (toCruz.getCurrentDivision().equals(division)) {
            if (toCruz.isCarryingTarget()) {
                System.out.println("    🕵️ To Cruz is here and is carrying the 🚩 target!");
            } else {
                System.out.println("    🕵️ To Cruz is here.");
            }
        }
    }

    /**
     * Displays detailed information about "To Cruz", including health, power, inventory, and
     * whether "To Cruz" is carrying the target.
     */
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

    /**
     * Attempts to collect the target in the current division, if no enemies are present.
     * If the target is present and not yet taken, it is assigned to To Cruz and removed from the mission.
     * Displays appropriate messages based on the target's availability and enemy presence.
     */
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
            lastMissionInfo.enqueue("To Cruz captured the target in " + currentDivision.getName());
        } else {
            System.out.println("No target available in this division.");
        }
    }

    /**
     * Collects all items available in the current division and adds them to To Cruz's inventory.
     * Items are removed from the mission's list once collected. If there are no items, it notifies the user.
     */
    public void collectItems() {
        IDivision currentDivision = toCruz.getCurrentDivision();
        UnorderedListADT<IItem> itemsInDivision = mission.getItemsByDivision(currentDivision);

        for (IItem item : itemsInDivision) {
            if (item.getType() == ItemType.LIFE_KIT) {
                toCruz.addHealthKit(item);
                lastMissionInfo.enqueue("To Cruz collected a life kit in " + currentDivision.getName() + " with " + item.getRecoveryPoints() + " recovery points.");
                mission.getItems().remove(item);
            } else if (item.getType() == ItemType.BULLET_PROOF_VEST) {
                toCruz.consumeBulletProofVest(item);
                lastMissionInfo.enqueue("To Cruz collected a bullet proof vest in " + currentDivision.getName() + " with " + item.getExtraPoints() + " extra points.");
                mission.getItems().remove(item);
            }
        }
        if (itemsInDivision.isEmpty()) {
            System.out.println("No items available to pick up in this division.");
        }
    }

    /**
     * Initiates an attack on all enemies present in the current division by To Cruz.
     * After the attack, the enemies outside the current division are moved to a nearby division.
     * If there are no enemies, it notifies the user.
     */
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
            lastMissionInfo.enqueue("To Cruz attacked enemies in " + currentDivision.getName());
            System.out.println("To Cruz attacked enemies in the current division!");
        } else {
            System.out.println("No enemies available in this division to attack!");
        }
    }

    /**
     * Simulates an attack on To Cruz by all enemies present in the current division.
     * If no enemies are present, it notifies the user.
     */
    public void attackToCruz() {
        IDivision currentDivision = toCruz.getCurrentDivision();
        UnorderedListADT<IEnemy> enemiesInDivision = mission.getEnemiesByDivision(currentDivision);

        if (!enemiesInDivision.isEmpty()) {
            for (IEnemy enemy : enemiesInDivision) {
                enemy.attackToCruz(toCruz);
                lastMissionInfo.enqueue("To Cruz attacked by " + enemy.getName() + " in " + currentDivision.getName() + " with " + enemy.getPower() + " power.");
            }

            System.out.println("ToCruz attacked by enemies!");
        } else {
            System.out.println("No enemies available in this division, to attack To Cruz!");
        }
    }

    /**
     * Moves To Cruz to a nearby division selected by the user.
     * Displays a list of available nearby divisions and prompts the user to choose one.
     * If the division is successfully changed, it prints a success message; otherwise, it indicates failure.
     */
    public void moveToNearbyDivision() {
        IDivision currentDivision = toCruz.getCurrentDivision();
        ArrayUnorderedList<IDivision> nearbyDivisions = building.getNeighbors(currentDivision);

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
            lastMissionInfo.enqueue("To Cruz moved to " + newDivision.getName());
        } else {
            System.out.println("Failed to move to the new division.");
        }
    }

    /**
     * Moves a specified enemy to a random nearby division. If no nearby divisions are available,
     * the enemy stays in the current division. If the enemy ends up in To Cruz's division, it attacks To Cruz.
     *
     * @param enemy The enemy to move.
     * @param toCruzCurrentDivision The current division of To Cruz.
     */
    private void moveEnemyToRandomNearbyDivision(IEnemy enemy, IDivision toCruzCurrentDivision) {
        IDivision currentDivision = enemy.getCurrentDivision();
        ArrayUnorderedList<IDivision> nearbyDivisions = building.getNeighbors(currentDivision);

        if (!nearbyDivisions.isEmpty()) {
            int randomIndex = (int) (Math.random() * nearbyDivisions.size());
            IDivision newDivision = nearbyDivisions.getElement(randomIndex);

            enemy.setCurrentDivision(newDivision);
            System.out.println("════════════════════════════════════════════════════");
            System.out.printf("Enemy '%s' moved from '%s' to '%s'.%n",
                    enemy.getName(), currentDivision.getName(), newDivision.getName());
            System.out.println("════════════════════════════════════════════════════");
            lastMissionInfo.enqueue("Enemy " + enemy.getName() + " moved from " + currentDivision.getName() + " to " + newDivision.getName());

            if (newDivision.equals(toCruzCurrentDivision)) {
                enemy.attackToCruz(toCruz);
                System.out.println("═══════════════════════════════════════════════");
                System.out.println("ToCruz attacked by new enemy in this division!");
                System.out.println("═══════════════════════════════════════════════");
                lastMissionInfo.enqueue("To Cruz attacked by new enemy in" + newDivision.getName());
            }
        } else {
            System.out.printf("Enemy '%s' remains in '%s' as there are no nearby divisions.%n",
                    enemy.getName(), currentDivision.getName());
        }
    }

    /**
     * Moves To Cruz to a new division, provided there are no enemies in the current division.
     * If successful, it updates To Cruz's current division and prints the new division's name.
     * If there are enemies in the current division, the move is prevented, and an error message is displayed.
     *
     * @param newDivision The division To Cruz is attempting to move to.
     * @return true if the move is successful, false otherwise.
     */
    public boolean moveToNewDivision(IDivision newDivision) {
        if (hasEnemiesInCurrentDivision()) {
            System.out.println("There are enemies in the current division. You cannot move yet.");
            return false;
        }

        toCruz.setCurrentDivision(newDivision);
        System.out.println("You moved to the new division: " + newDivision.getName());
        lastMissionInfo.enqueue("To Cruz moved to " + newDivision.getName());

        printInGameDivisionFight();
        return true;
    }

    /**
     * Prompts the user to select a valid division from a list of available divisions.
     * Ensures that the choice entered is a valid integer within the given range.
     * If the input is invalid, it continues prompting the user until a valid choice is made.
     *
     * @param size The number of available divisions to choose from.
     * @return The valid division choice made by the user.
     */
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

    /**
     * Checks if there are any enemies present in the current division of To Cruz.
     *
     * @return true if there are enemies in the current division, false otherwise.
     */
    public boolean hasEnemiesInCurrentDivision() {
        IDivision currentDivision = toCruz.getCurrentDivision();
        UnorderedListADT<IEnemy> enemiesInDivision = mission.getEnemiesByDivision(currentDivision);

        return !enemiesInDivision.isEmpty();
    }

    /**
     * Prints the status of the current division fight, including an attack on enemies in the division.
     * This method invokes the attackEnemiesInCurrentDivision() method to perform the attack.
     */
    public void printInGameDivisionFight() {
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("           IN-GAME Current Division Fight           ");
        System.out.println("════════════════════════════════════════════════════");

        attackEnemiesInCurrentDivision();

        System.out.println("════════════════════════════════════════════════════");
    }

    /**
     * Finalizes the mission, checking if all conditions for a successful mission are met.
     * To finalize, To Cruz must have captured the target, be in an entry point, and the health record is saved.
     * The mission success message is printed and the mission records are saved to a JSON file.
     *
     * @return true if the mission is successfully finalized, false otherwise.
     */
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

        lastMissionInfo.enqueue("Mission finalized with " + healthRecord + " health points.");

        try {
            saveMissionRecordsToJson();
        } catch (IOException e) {
            System.out.println("Error saving mission records: " + e.getMessage());
        }
        try {
            saveLastMissionMovements(lastMissionInfo);
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

    /**
     * Checks if To Cruz has died by evaluating if his health is zero or below.
     * If To Cruz's health is zero or less, the game ends and a message is displayed.
     *
     * @return true if To Cruz has died, false otherwise.
     */
    public boolean toCruzDies() {
        if (toCruz.getHealth() <= 0) {
            lastMissionInfo.enqueue("To Cruz has fallen with " + toCruz.getHealth() + " health points.");
            try {
                saveLastMissionMovements(lastMissionInfo);
            } catch (IOException e) {
                System.out.println("Error saving mission records: " + e.getMessage());
            }
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
}