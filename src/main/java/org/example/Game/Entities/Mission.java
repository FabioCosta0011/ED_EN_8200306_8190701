package org.example.Game.Entities;

import org.example.Game.Entities.ENUMS.ItemType;
import org.example.Game.Entities.Interfaces.*;
import org.example.Structures.Implementations.ArrayOrderedList;
import org.example.Structures.Implementations.ArrayUnorderedList;
import org.example.Structures.Implementations.LinkedQueue;
import org.example.Structures.Interfaces.*;

public class Mission implements IMission {

    private String missionCode;
    private int version;
    private final BuildingGraph<IDivision> divisions;
    private final UnorderedListADT<IEnemy> enemies;
    private final UnorderedListADT<IDivision> entryPoints;
    private ITarget target;
    private final UnorderedListADT<IItem> items;
    private final OrderedListADT<IRecord> records;
    private int points = 100;

    public Mission() {
        this.missionCode = null;
        this.version = 0;
        this.target = null;
        this.divisions = new BuildingGraph<>();
        this.enemies = new ArrayUnorderedList<>();
        this.entryPoints = new ArrayUnorderedList<>();
        this.items = new ArrayUnorderedList<>();
        this.records = new ArrayOrderedList<>();
    }

    @Override
    public String getMissionCode() {
        return missionCode;
    }

    @Override
    public void setMissionCode(String missionCode) {
        this.missionCode = missionCode;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public void setMissionVersion(int version) {
        this.version = version;
    }

    @Override
    public BuildingGraph<IDivision> getDivisions() {
        return divisions;
    }

    @Override
    public UnorderedListADT<IEnemy> getEnemies() {
        return enemies;
    }

    @Override
    public UnorderedListADT<IDivision> getEntryPoints() {
        return entryPoints;
    }

    @Override
    public ITarget getTarget() {
        return target;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public OrderedListADT<IRecord> getRecords() {
        return records;
    }

    @Override
    public void setMissionTarget(ITarget target) {
        this.target = target;
    }

    @Override
    public UnorderedListADT<IItem> getItems() {
        return items;
    }

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

        // TODO ALTERAR PARA TER EM CONTA O IMPACTO APOS O TO CRUZ ENTRAR NA SALA
        for (IEnemy enemy : this.getEnemiesByDivision(division)) {
            impact -= enemy.getPower();
        }

        return impact;
    }


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

    private boolean hasItems(IDivision division) {
        UnorderedListADT<IItem> itemsInDivision = this.getItemsByDivision(division);
        return !itemsInDivision.isEmpty();
    }

}

