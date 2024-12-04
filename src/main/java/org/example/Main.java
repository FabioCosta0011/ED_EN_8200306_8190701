package org.example;

import org.example.Game.Entities.Game;
import org.example.Game.Entities.Interfaces.IDivision;
import org.example.Game.Entities.Interfaces.IEnemy;
import org.example.Game.Entities.Interfaces.IItem;
import org.example.Game.Entities.Mission;
import org.example.Structures.Implementations.ArrayUnorderedList;
import org.example.Structures.Interfaces.UnorderedListADT;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();

        try {
            Mission mission = game.loadMissionFromJson("mission.json");

            System.out.println("==== Mission Information ====");
            System.out.println("Mission Code: " + mission.getMissionCode());
            System.out.println("Version: " + mission.getVersion());
            System.out.println("Target: " + mission.getTarget().getType());
            System.out.println();

            System.out.println("==== Target Information ====");
            System.out.println("Target Division: " + mission.getTarget().getDivision().getName());
            System.out.println("Target Type: " + mission.getTarget().getType());
            System.out.println();

            System.out.println("==== Divisions and Connections ====");
            ArrayUnorderedList<IDivision> divisions = mission.getDivisions().getAllVertices();
            for (int i = 0; i < divisions.size(); i++) {
                IDivision division = divisions.getElement(i);
                System.out.append("Div ").append(String.valueOf(i + 1)).append(": ").append(division.getName()).append("\n");

                ArrayUnorderedList<IDivision> neighbors = mission.getDivisions().getNeighbors(division);
                if (!neighbors.isEmpty()) {
                    for (int j = 0; j < neighbors.size(); j++) {
                        IDivision neighbor = neighbors.getElement(j);
                        System.out.append(" -> Connected to ").append(neighbor.getName()).append("\n");
                    }
                } else {
                    System.out.println(" -> No connections available.");
                }

                ArrayUnorderedList<IEnemy> enemiesInDivision = mission.getEnemiesByDivision(division);
                if (!enemiesInDivision.isEmpty()) {
                    for (IEnemy enemy : enemiesInDivision) {
                        System.out.println(" -> Enemy: " + enemy.getName() + " (Power: " + enemy.getPower() + ")");
                    }
                } else {
                    System.out.println(" -> No enemies in this division.");
                }

                ArrayUnorderedList<IItem> itemsInDivision = mission.getItemsByDivision(division);
                if (!itemsInDivision.isEmpty()) {
                    for (IItem item : itemsInDivision) {
                        switch (item.getType()) {
                            case LIFE_KIT:
                                System.out.println(" -> Item: " + item.getType() + " (Recovered Points: " + item.getRecoveryPoints() + ")");
                                break;
                            case BULLET_PROOF_VEST:
                                System.out.println(" -> Item: " + item.getType() + " (Extra Points: " + item.getExtraPoints() + ")");
                                break;
                            default:
                                System.out.println(" -> Item: " + item.getType());
                        }
                    }
                } else {
                    System.out.println(" -> No items in this division.");
                }

                System.out.println();
            }

            System.out.println("==== Entry and Exit Points ====");
            UnorderedListADT<IDivision> entryPoints = mission.getEntryPoints();
            if (!entryPoints.isEmpty()) {
                for (IDivision entryPoint : entryPoints) {
                    System.out.println(" -> Entry: " + entryPoint.getName());
                }
            } else {
                System.out.println(" -> No entry points available.");
            }

        } catch (IOException e) {
            System.err.println("Error loading mission: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
