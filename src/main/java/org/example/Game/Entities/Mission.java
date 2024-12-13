package org.example.Game.Entities;

import org.example.Game.Entities.ENUMS.ItemType;
import org.example.Game.Entities.Interfaces.*;
import org.example.Structures.Implementations.ArrayOrderedList;
import org.example.Structures.Implementations.ArrayUnorderedList;
import org.example.Structures.Implementations.LinkedQueue;
import org.example.Structures.Implementations.LinkedStack;
import org.example.Structures.Interfaces.*;

public class Mission {

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

    public String getMissionCode() {
        return missionCode;
    }

    public void setMissionCode(String missionCode) {
        this.missionCode = missionCode;
    }

    public int getVersion() {
        return version;
    }

    public void setMissionVersion(int version) {
        this.version = version;
    }

    public BuildingGraph<IDivision> getDivisions() {
        return divisions;
    }

    public UnorderedListADT<IEnemy> getEnemies() {
        return enemies;
    }

    public UnorderedListADT<IDivision> getEntryPoints() {
        return entryPoints;
    }

    public ITarget getTarget() {
        return target;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public OrderedListADT<IRecord> getRecords() {
        return records;
    }

    public void setMissionTarget(ITarget target) {
        this.target = target;
    }

    public UnorderedListADT<IItem> getItems() {
        return items;
    }

    public UnorderedListADT<IEnemy> getEnemiesByDivision(IDivision division) {
        UnorderedListADT<IEnemy> enemiesInDivision = new ArrayUnorderedList<>();

        for (IEnemy enemy : enemies) {
            if (enemy.getCurrentDivision().equals(division)) {
                enemiesInDivision.addToRear(enemy);
            }
        }

        return enemiesInDivision;
    }

    public UnorderedListADT<IEnemy> getAllEnemiesOutsideCurrentDivision(IDivision division, IToCruz toCruz) {
        UnorderedListADT<IEnemy> enemiesOutsideDivision = new ArrayUnorderedList<>();

        for (IEnemy enemy : enemies) {
            if (!enemy.getCurrentDivision().equals(toCruz.getCurrentDivision())) {
                enemiesOutsideDivision.addToRear(enemy);
            }
        }
        return enemiesOutsideDivision;
    }

    public UnorderedListADT<IItem> getItemsByDivision(IDivision division) {
        UnorderedListADT<IItem> itemsInDivision = new ArrayUnorderedList<>();

        for (IItem item : items) {
            if (item.getDivision().equals(division)) {
                itemsInDivision.addToRear(item);
            }
        }

        return itemsInDivision;
    }

    public QueueADT<IDivision> findBestPathToTarget(IDivision currentDivision, IDivision targetDivision) {
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

    public QueueADT<IDivision> verifyPathToEntry(QueueADT<IDivision> pathStack, int initialPoints) {
        QueueADT<IDivision> verifiedPath = new LinkedQueue<>();
        ArrayUnorderedList<IDivision> visited = new ArrayUnorderedList<>();
        int pointsRemaining = initialPoints;

        while (!pathStack.isEmpty()) {
            IDivision current = pathStack.dequeue();

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

    private int calculatePoints(UnorderedListADT<IDivision> path) {
        int points = 0;
        for (IDivision division : path) {
            UnorderedListADT<IItem> items = this.getItemsByDivision(division);
            UnorderedListADT<IEnemy> enemies = this.getEnemiesByDivision(division);

            for (IItem item : items) {
                if (item.getType() == ItemType.LIFE_KIT) {
                    points += item.getRecoveryPoints();
                } else if (item.getType() == ItemType.BULLET_PROOF_VEST) {
                    points += item.getExtraPoints();
                }
            }

            for (IEnemy enemy : enemies) {
                points -= enemy.getPower();
            }
        }
        return points;
    }

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


    public QueueADT<IDivision> findBestPathFromMultipleEntryPoints(UnorderedListADT<IDivision> entryPoints, IDivision targetDivision) {
        QueueADT<IDivision> bestPath = null;
        int maxPoints = -1;

        for (IDivision entryPoint : entryPoints) {
            QueueADT<IDivision> currentPath = findBestPathToTarget(entryPoint, targetDivision);

            if (currentPath != null && !currentPath.isEmpty()) {
                int currentPoints = calculatePathPoints(currentPath);

                if (currentPoints > maxPoints) {
                    maxPoints = currentPoints;
                    bestPath = currentPath;
                }
            }
        }

        return bestPath;
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


    public QueueADT<IDivision> findBestRouteFromMultipleEntryPoints(UnorderedListADT<IDivision> entryPoints, IDivision targetDivision) {
        QueueADT<IDivision> bestPathToTarget = null;
        int maxPointsToTarget = -1; // Inicializa como -1 para garantir que qualquer caminho válido seja aceito.

        // Verifica o melhor caminho de entrada até o alvo
        for (IDivision entryPoint : entryPoints) {
            QueueADT<IDivision> currentPathToTarget = findBestPathToTarget(entryPoint, targetDivision);

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
            QueueADT<IDivision> currentPathFromTarget = findBestPathToTarget(targetDivision, entryPoint);

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

    private int calculatePathPoints2(QueueADT<IDivision> path) {
        int totalPoints = 100; // Começa com 100 pontos de vida (valor inicial)
        QueueADT<IDivision> pathCopy = new LinkedQueue<>(); // Copia o caminho para preservá-lo

        while (!path.isEmpty()) {
            IDivision currentDivision = path.dequeue();
            pathCopy.enqueue(currentDivision);

            // Adicionar pontos dos itens e subtrair impacto dos inimigos
            totalPoints += calculateImpact(currentDivision);

            // Verificar se os pontos ficaram abaixo de zero (invalida o caminho)
            if (totalPoints <= 0) {
                return -1; // Caminho inválido
            }
        }

        // Restaurar o caminho original
        while (!pathCopy.isEmpty()) {
            path.enqueue(pathCopy.dequeue());
        }

        return totalPoints;
    }



}

