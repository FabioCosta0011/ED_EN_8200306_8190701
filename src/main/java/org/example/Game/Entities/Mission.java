package org.example.Game.Entities;

import org.example.Game.Entities.ENUMS.ItemType;
import org.example.Game.Entities.Interfaces.*;
import org.example.Structures.Implementations.ArrayOrderedList;
import org.example.Structures.Implementations.ArrayUnorderedList;
import org.example.Structures.Implementations.LinkedQueue;
import org.example.Structures.Interfaces.*;

/**
 * The Mission class represents a mission in the game. It holds information about
 * the mission's divisions, enemies, items, target, and records. It provides methods
 * to manage mission data, including retrieving items, enemies, finding the best path,
 * and calculating impacts of divisions on the mission's progress.
 */
public class Mission implements IMission {

    /**
     * The unique code for this mission.
     */
    private String missionCode;

    /**
     * The version number of the mission.
     */
    private int version;

    /**
     * The divisions involved in the mission.
     */
    private final Building<IDivision> divisions;

    /**
     * The list of enemies in the mission.
     */
    private final UnorderedListADT<IEnemy> enemies;

    /**
     * The list of entry points for the mission.
     */
    private final UnorderedListADT<IDivision> entryPoints;

    /**
     * The target of the mission.
     */
    private ITarget target;

    /**
     * The list of items available in the mission.
     */
    private final UnorderedListADT<IItem> items;

    /**
     * The records of the mission, such as actions or progress logs.
     */
    private final OrderedListADT<IRecord> records;

    /**
     * The number of points associated with the mission.
     */
    private int points = 100;

    /**
     * Default constructor that initializes the mission with default values.
     * Creates empty lists for divisions, enemies, entry points, items, and records.
     */
    public Mission() {
        this.missionCode = null;
        this.version = 0;
        this.target = null;
        this.divisions = new Building<>();
        this.enemies = new ArrayUnorderedList<>();
        this.entryPoints = new ArrayUnorderedList<>();
        this.items = new ArrayUnorderedList<>();
        this.records = new ArrayOrderedList<>();
    }

    /**
     * Gets the mission code.
     *
     * @return The mission code.
     */
    @Override
    public String getMissionCode() {
        return missionCode;
    }

    /**
     * Sets the mission code.
     *
     * @param missionCode The mission code to set.
     */
    @Override
    public void setMissionCode(String missionCode) {
        this.missionCode = missionCode;
    }

    /**
     * Gets the mission version.
     *
     * @return The version number of the mission.
     */
    @Override
    public int getVersion() {
        return version;
    }

    /**
     * Sets the mission version.
     *
     * @param version The version number to set.
     */
    @Override
    public void setMissionVersion(int version) {
        this.version = version;
    }

    /**
     * Gets the divisions of the mission.
     *
     * @return The divisions of the mission.
     */
    @Override
    public Building<IDivision> getDivisions() {
        return divisions;
    }

    /**
     * Gets the enemies in the mission.
     *
     * @return The list of enemies in the mission.
     */
    @Override
    public UnorderedListADT<IEnemy> getEnemies() {
        return enemies;
    }

    /**
     * Gets the entry points of the mission.
     *
     * @return The entry points of the mission.
     */
    @Override
    public UnorderedListADT<IDivision> getEntryPoints() {
        return entryPoints;
    }

    /**
     * Gets the target of the mission.
     *
     * @return The target of the mission.
     */
    @Override
    public ITarget getTarget() {
        return target;
    }

    /**
     * Gets the number of points associated with the mission.
     *
     * @return The number of points for the mission.
     */
    @Override
    public int getPoints() {
        return points;
    }

    /**
     * Sets the points for the mission.
     *
     * @param points The number of points to set for the mission.
     */
    @Override
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Gets the records associated with the mission.
     *
     * @return The records of the mission.
     */
    @Override
    public OrderedListADT<IRecord> getRecords() {
        return records;
    }

    /**
     * Sets the target for the mission.
     *
     * @param target The target to set for the mission.
     */
    @Override
    public void setMissionTarget(ITarget target) {
        this.target = target;
    }

    /**
     * Gets the items in the mission.
     *
     * @return The list of items in the mission.
     */
    @Override
    public UnorderedListADT<IItem> getItems() {
        return items;
    }

    /**
     * Retrieves all enemies in a given division.
     *
     * @param division The division to search for enemies.
     * @return A list of enemies in the specified division.
     */
    @Override
    public UnorderedListADT<IEnemy> getEnemiesByDivision(IDivision division) {
        UnorderedListADT<IEnemy> enemiesInDivision = new ArrayUnorderedList<>();

        for (IEnemy enemy : enemies) {
            if (enemy.getCurrentDivision().equals(division)) {
                enemiesInDivision.addToRear(enemy);
            }
        }

        return enemiesInDivision;
    }

    /**
     * Retrieves all enemies not in the current division and not in a specified division.
     *
     * @param division The current division to exclude.
     * @param toCruz The object that contains the target division for the enemy search.
     * @return A list of enemies outside the current division.
     */
    @Override
    public UnorderedListADT<IEnemy> getAllEnemiesOutsideCurrentDivision(IDivision division, IToCruz toCruz) {
        UnorderedListADT<IEnemy> enemiesOutsideDivision = new ArrayUnorderedList<>();

        for (IEnemy enemy : enemies) {
            if (!enemy.getCurrentDivision().equals(toCruz.getCurrentDivision())) {
                enemiesOutsideDivision.addToRear(enemy);
            }
        }
        return enemiesOutsideDivision;
    }

    /**
     * Retrieves all items in a specified division.
     *
     * @param division The division to search for items.
     * @return A list of items in the specified division.
     */
    @Override
    public UnorderedListADT<IItem> getItemsByDivision(IDivision division) {
        UnorderedListADT<IItem> itemsInDivision = new ArrayUnorderedList<>();

        for (IItem item : items) {
            if (item.getDivision().equals(division)) {
                itemsInDivision.addToRear(item);
            }
        }

        return itemsInDivision;
    }

    /**
     * Finds the best path from a given division to a target division, considering points impact.
     *
     * @param currentDivision The current division to start the path search.
     * @param targetDivision The division where the path should lead.
     * @return A queue representing the best path to the target division.
     */
    @Override
    public QueueADT<IDivision> findBestPath(IDivision currentDivision, IDivision targetDivision) {
        QueueADT<IDivision> queue = new LinkedQueue<>();
        ArrayUnorderedList<IDivision> visited = new ArrayUnorderedList<>();
        ArrayUnorderedList<Integer> pointsRemaining = new ArrayUnorderedList<>();
        ArrayUnorderedList<IDivision> predecessors = new ArrayUnorderedList<>();

        queue.enqueue(currentDivision);
        visited.addToFront(currentDivision);
        pointsRemaining.addToRear(points);
        predecessors.addToRear(null);

        while (!queue.isEmpty()) {
            IDivision current = queue.dequeue();
            int currentPoints = pointsRemaining.getElement(visited.find(current));

            if (current.equals(targetDivision)) {
                break;
            }

            ArrayUnorderedList<IDivision> neighbors = this.getDivisions().getNeighbors(current);
            for (IDivision neighbor : neighbors) {
                if (visited.contains(neighbor)) {
                    continue;
                }

                int pointsImpact = calculateImpact(neighbor);

                if (currentPoints + pointsImpact > 0) {
                    int newPoints = currentPoints + pointsImpact;

                    pointsRemaining.addToRear(newPoints);
                    predecessors.addToRear(current);
                    queue.enqueue(neighbor);
                    visited.addToRear(neighbor);
                    setPoints(newPoints);
                }
            }
        }

        QueueADT<IDivision> bestPath = new LinkedQueue<>();
        IDivision step = targetDivision;
        while (step != null) {
            bestPath.enqueue(step);
            int predecessorIndex = visited.find(step);
            step = predecessors.getElement(predecessorIndex);
        }
        return bestPath;
    }

    /**
     * Finds the best path to a Life Kit from the given division.
     * This method uses a breadth-first search (BFS) approach to explore the
     * neighboring divisions, calculating the points remaining based on the
     * impact of items and enemies encountered along the way.
     *
     * @param currentDivision the division from which the pathfinding starts
     * @return an unordered list containing the divisions of the best path to a Life Kit,
     *         or an empty list if no such path exists
     */
    @Override
    public UnorderedListADT<IDivision> findBestPathToLifeKit(IDivision currentDivision) {
        QueueADT<IDivision> queue = new LinkedQueue<>();
        ArrayUnorderedList<IDivision> visited = new ArrayUnorderedList<>();
        ArrayUnorderedList<Integer> pointsRemaining = new ArrayUnorderedList<>();
        ArrayUnorderedList<IDivision> predecessors = new ArrayUnorderedList<>();

        queue.enqueue(currentDivision);
        visited.addToFront(currentDivision);
        pointsRemaining.addToRear(100);
        predecessors.addToRear(null);

        while (!queue.isEmpty()) {
            IDivision current = queue.dequeue();
            int currentPoints = pointsRemaining.getElement(visited.find(current));

            ArrayUnorderedList<IItem> items = (ArrayUnorderedList<IItem>) this.getItemsByDivision(current);
            if (hasItems(current)) {
                return reconstructPath(predecessors, visited, current);
            }

            ArrayUnorderedList<IDivision> neighbors = this.getDivisions().getNeighbors(current);
            for (IDivision neighbor : neighbors) {
                if (visited.contains(neighbor)) {
                    continue;
                }

                int pointsImpact = calculateImpact(neighbor);

                if (currentPoints + pointsImpact > 0) {
                    int newPoints = currentPoints + pointsImpact;

                    pointsRemaining.addToRear(newPoints);
                    predecessors.addToRear(current);
                    queue.enqueue(neighbor);
                    visited.addToRear(neighbor);
                }
            }
        }
        return new ArrayUnorderedList<>();
    }

    /**
     * Finds the best route from multiple entry points to a target division,
     * then finds the best route back from the target division to the entry points.
     * The method calculates the best route by comparing the available paths based on
     * the total points (impact of items and enemies) along each route.
     *
     * @param entryPoints an unordered list of possible entry points to start the journey
     * @param targetDivision the target division where the path should end
     * @return a queue containing the divisions of the best complete path (to and from the target),
     *         or null if no valid path is found
     */
    @Override
    public QueueADT<IDivision> findBestRouteFromMultipleEntryPoints(UnorderedListADT<IDivision> entryPoints, IDivision targetDivision) {
        QueueADT<IDivision> bestPathToTarget = null;
        int maxPointsToTarget = -1;


        for (IDivision entryPoint : entryPoints) {
            QueueADT<IDivision> currentPathToTarget = findBestPath(entryPoint, targetDivision);

            if (currentPathToTarget != null && !currentPathToTarget.isEmpty()) {
                int currentPointsToTarget = calculatePathPoints(currentPathToTarget);

                if (currentPointsToTarget > maxPointsToTarget) {
                    maxPointsToTarget = currentPointsToTarget;
                    bestPathToTarget = currentPathToTarget;
                }
            }
        }

        // Agora que temos o melhor caminho para o alvo, verificamos o melhor caminho de volta
        QueueADT<IDivision> bestPathFromTarget = null;
        int maxPointsFromTarget = -1; // Inicializa como -1 para garantir que qualquer caminho válido seja aceito.

        // Encontrar o melhor caminho de volta para os pontos de entrada a partir do alvo
        for (IDivision entryPoint : entryPoints) {
            QueueADT<IDivision> currentPathFromTarget = findBestPath(targetDivision, entryPoint);

            if (currentPathFromTarget != null && !currentPathFromTarget.isEmpty()) {
                int currentPointsFromTarget = calculatePathPoints(currentPathFromTarget);

                if (currentPointsFromTarget > maxPointsFromTarget) {
                    maxPointsFromTarget = currentPointsFromTarget;
                    bestPathFromTarget = currentPathFromTarget;
                }
            }
        }

        // Se ambos os caminhos são encontrados, podemos retornar o melhor caminho completo (ida e volta)
        if (bestPathToTarget != null && bestPathFromTarget != null) {
            // Juntando os dois caminhos (ida e volta) em uma única fila
            QueueADT<IDivision> completePath = new LinkedQueue<>();

            // Adicionando o caminho de entrada para o alvo
            while (!bestPathToTarget.isEmpty()) {
                completePath.enqueue(bestPathToTarget.dequeue());
            }

            // Adicionando o caminho de volta (do alvo até a entrada)
            while (!bestPathFromTarget.isEmpty()) {
                completePath.enqueue(bestPathFromTarget.dequeue());
            }

            return completePath; // Retorna o caminho completo com maior número de pontos
        }

        return null; // Se não houver caminho válido
    }

    /**
     * Verifies the validity of a given path to an entry point, ensuring that the points
     * along the path do not drop below zero. The method uses the impact of items and enemies
     * to calculate the remaining points as the path is traversed.
     *
     * @param pathQueue the queue containing the divisions representing the path
     * @param initialPoints the initial number of points available at the start
     * @return a queue containing the divisions of the verified path, or null if the path becomes invalid
     */
    @Override
    public QueueADT<IDivision> verifyPathToEntry(QueueADT<IDivision> pathQueue, int initialPoints) {
        QueueADT<IDivision> verifiedPath = new LinkedQueue<>();
        ArrayUnorderedList<IDivision> visited = new ArrayUnorderedList<>();
        int pointsRemaining = initialPoints;

        while (!pathQueue.isEmpty()) {
            IDivision current = pathQueue.dequeue();

            if (visited.contains(current)) {
                break;
            }

            int pointsImpact = calculateImpact(current);
            pointsRemaining += pointsImpact;

            if (pointsRemaining <= 0) {
                break;
            }

            verifiedPath.enqueue(current);
            visited.addToRear(current);
        }

        if (verifiedPath.isEmpty()) {
            return null;
        }

        return verifiedPath;
    }

    /**
     * Calculates the impact of a division on the remaining points.
     * The impact is calculated based on items found in the division (e.g., LIFE_KIT or BULLET_PROOF_VEST)
     * and enemies that subtract from the available points.
     *
     * @param division The division whose impact on the points is being calculated.
     * @return The impact on points, which could be positive (recovery from items) or negative (damage from enemies).
     */
    private int calculateImpact(IDivision division) {
        int impact = 0;

        // Add points from items
        for (IItem item : this.getItemsByDivision(division)) {
            if (item.getType() == ItemType.LIFE_KIT) {
                impact += item.getRecoveryPoints();
            } else if (item.getType() == ItemType.BULLET_PROOF_VEST) {
                impact += item.getExtraPoints();
            }
        }
         //Remove points from enemies
        for (IEnemy enemy : this.getEnemiesByDivision(division)) {
            impact -= enemy.getPower();
        }

        return impact;
    }

    /**
     * Calculates the total points of a path by summing the impact of each division encountered.
     * This method ensures that if the points become zero or negative during the path,
     * it returns -1 to indicate that the path is not viable.
     *
     * @param path The queue representing the divisions in the path.
     * @return The total points remaining after traversing the path. If the path is invalid, -1 is returned.
     */
    private int calculatePathPoints(QueueADT<IDivision> path) {
        int totalPoints = 100;
        QueueADT<IDivision> pathCopy = new LinkedQueue<>();

        while (!path.isEmpty()) {
            IDivision currentDivision = path.dequeue();
            pathCopy.enqueue(currentDivision);

            totalPoints += calculateImpact(currentDivision);

            if (totalPoints <= 0) {
                return -1;
            }
        }

        while (!pathCopy.isEmpty()) {
            path.enqueue(pathCopy.dequeue());
        }
        return totalPoints;
    }

    /**
     * Checks if there are any items in the specified division.
     * This method helps to verify if an item (e.g., Life Kit) is present in the division.
     *
     * @param division The division to check for items.
     * @return True if the division contains any items, false otherwise.
     */
    private boolean hasItems(IDivision division) {
        UnorderedListADT<IItem> itemsInDivision = this.getItemsByDivision(division);
        return !itemsInDivision.isEmpty();
    }

    /**
     * Reconstructs the path to the target division based on the predecessors.
     * This method traces back from the target division to the starting division using the predecessors.
     *
     * @param predecessors A list of divisions that represent the predecessors of each division.
     * @param visited A list of divisions that have been visited.
     * @param target The target division to reconstruct the path to.
     * @return A list representing the reconstructed path to the target division.
     */
    private UnorderedListADT<IDivision> reconstructPath(ArrayUnorderedList<IDivision> predecessors,
                                                        ArrayUnorderedList<IDivision> visited,
                                                        IDivision target) {
        UnorderedListADT<IDivision> path = new ArrayUnorderedList<>();
        IDivision step = target;

        while (step != null) {
            path.addToFront(step);
            int predecessorIndex = visited.find(step);
            step = predecessors.getElement(predecessorIndex);
        }

        return path;
    }

}

