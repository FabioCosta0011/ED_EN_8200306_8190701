package org.example.Game.Entities;

import org.example.Game.Entities.ENUMS.ItemType;
import org.example.Game.Entities.Interfaces.IDivision;
import org.example.Game.Entities.Interfaces.IEnemy;
import org.example.Game.Entities.Interfaces.IItem;
import org.example.Structures.Implementations.ArrayUnorderedList;
import org.example.Structures.Implementations.LinkedQueue;
import org.example.Structures.Interfaces.QueueADT;
import org.example.Structures.Interfaces.UnorderedListADT;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MissionSimulation {

    private Mission mission;

    public MissionSimulation(Mission mission) {
        this.mission = mission;
    }

    public void simulateBestRoute() {
        // A lista de entradas e saídas é a mesma
        UnorderedListADT<IDivision> entryExitPoints = mission.getEntryPoints();
        IDivision targetDivision = mission.getTarget().getDivision();

        // Rodar a simulação para cada ponto de entrada/saída
        for (IDivision entryExitPoint : entryExitPoints) {
            System.out.println("Simulando trajeto a partir de: " + entryExitPoint.getName());

            // Caminho de ida (entrando no edifício e indo até o alvo)
            UnorderedListADT<IDivision> bestPathToTarget = findBestPath(entryExitPoint, targetDivision);
            int pointsOnTheWay = calculatePoints(bestPathToTarget);

            // Caminho de volta (retornando ao ponto de entrada/saída)
            UnorderedListADT<IDivision> bestPathBack = findBestPath(targetDivision, entryExitPoint);
            int pointsOnTheWayBack = calculatePoints(bestPathBack);

            // Verificar se a missão pode ser concluída com sucesso
            if (pointsOnTheWay >= 0 && pointsOnTheWayBack >= 0) {
                System.out.println("Trajeto possível com sucesso! Pontos restantes: " + (pointsOnTheWay + pointsOnTheWayBack));
            } else {
                System.out.println("Não é possível realizar o trajeto sem comprometer o sucesso da missão.");
            }
        }
    }

    public UnorderedListADT<IDivision> findBestPath(IDivision start, IDivision target) {
        // Map to track visited divisions
        Map<IDivision, Integer> pointsRemaining = new HashMap<>();
        Map<IDivision, IDivision> predecessors = new HashMap<>();
        Set<IDivision> visited = new HashSet<>();
        QueueADT<IDivision> queue = new LinkedQueue<>();

        // Initialize
        pointsRemaining.put(start, 100); // Example: Tó Cruz starts with 100 points
        queue.enqueue(start);
        visited.add(start); // Mark the start division as visited

        while (!queue.isEmpty()) {
            IDivision current = queue.dequeue();

            // Stop when we reach the target
            if (current.equals(target)) {
                break;
            }

            // Explore neighbors
            for (IDivision neighbor : mission.getDivisions().getNeighbors(current)) {
                if (visited.contains(neighbor)) {
                    continue; // Skip already visited divisions
                }

                int currentPoints = pointsRemaining.get(current);
                int pointsImpact = calculateImpact(neighbor); // Calculate impact of moving to this neighbor

                // Only continue if Tó Cruz has enough points
                if (currentPoints + pointsImpact > 0) {
                    int newPoints = currentPoints + pointsImpact;

                    // Update if this path is better
                    if (!pointsRemaining.containsKey(neighbor) || newPoints > pointsRemaining.get(neighbor)) {
                        pointsRemaining.put(neighbor, newPoints);
                        predecessors.put(neighbor, current);
                        queue.enqueue(neighbor);
                        visited.add(neighbor); // Mark neighbor as visited
                    }
                }
            }
        }

        // Reconstruct the best path
        UnorderedListADT<IDivision> bestPath = new ArrayUnorderedList<>();
        for (IDivision step = target; step != null; step = predecessors.get(step)) {
            bestPath.addToRear(step);
        }

        // Reverse the path since we built it backwards
        UnorderedListADT<IDivision> finalPath = new ArrayUnorderedList<>();
        while (!bestPath.isEmpty()) {
            finalPath.addToRear(bestPath.removeFirst());
        }

        return finalPath;
    }


    private int calculateImpact(IDivision division) {
        int impact = 0;

        // Add points from items
        for (IItem item : mission.getItemsByDivision(division)) {
            if (item.getType() == ItemType.LIFE_KIT) {
                impact += item.getRecoveryPoints();
            } else if (item.getType() == ItemType.BULLET_PROOF_VEST) {
                impact += item.getExtraPoints();
            }
        }

        // Subtract points from enemies
        for (IEnemy enemy : mission.getEnemiesByDivision(division)) {
            impact -= enemy.getPower();
        }

        return impact;
    }

    private int calculatePoints(UnorderedListADT<IDivision> path) {
        int points = 0;
        for (IDivision division : path) {
            // Considerar os itens e inimigos da divisão
            UnorderedListADT<IItem> items = mission.getItemsByDivision(division);
            UnorderedListADT<IEnemy> enemies = mission.getEnemiesByDivision(division);

            // Para cada item, adicionar os pontos de recuperação ou extra
            for (IItem item : items) {
                if (item.getType() == ItemType.LIFE_KIT) {
                    points += item.getRecoveryPoints();
                } else if (item.getType() == ItemType.BULLET_PROOF_VEST) {
                    points += item.getExtraPoints();
                }
            }

            // Para cada inimigo, subtrair os pontos de poder
            for (IEnemy enemy : enemies) {
                points -= enemy.getPower();
            }
        }
        return points;
    }

}
