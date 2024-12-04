package org.example.Game.Entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Game.Entities.Interfaces.IDivision;
import org.example.Game.Entities.Interfaces.IEnemy;
import org.example.Game.Entities.Interfaces.IItem;
import org.example.Game.Entities.Interfaces.ITarget;

import java.io.IOException;
import java.io.InputStream;

public class Game {

    public Mission loadMissionFromJson(String filePath) throws IOException {
        // Load the JSON file from resources
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);

        if (inputStream == null) {
            throw new IOException("File not found: " + filePath);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(inputStream);

        // Parse mission details
        String missionCode = root.get("cod-missao").asText();
        int version = root.get("versao").asInt();

        // Parse target
        JsonNode targetNode = root.get("alvo");
        String targetDivision = targetNode.get("divisao").asText();
        // Map the target type manually to match enum
        String targetType = targetNode.get("tipo").asText();  // Mapping string to enum value
        ITarget target = new Target(new Division(targetDivision), targetType);

        // Create mission object
        Mission mission = new Mission(missionCode, version, target);

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
            int health = enemyNode.get("vida").asInt();
            String division = enemyNode.get("divisao").asText();
            IEnemy enemy = new Enemy(name, power, health, new Division(division));
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
}
