package org.example.Game.Entities;

import org.example.Game.Entities.ENUMS.ItemType;
import org.example.Game.Entities.Interfaces.*;
import org.example.Structures.Implementations.ArrayUnorderedList;
import org.example.Structures.Implementations.LinkedQueue;
import org.example.Structures.Interfaces.QueueADT;
import org.example.Structures.Interfaces.UnorderedListADT;

public class Mission {

    private String missionCode;
    private int version;
    private final BuildingGraph<IDivision> divisions;
    private final UnorderedListADT<IEnemy> enemies;
    private final UnorderedListADT<IDivision> entryPoints;
    private ITarget target;
    private final UnorderedListADT<IItem> items;

    public Mission() {
        this.missionCode = null;
        this.version = 0;
        this.target = null;
        this.divisions = new BuildingGraph<>();
        this.enemies = new ArrayUnorderedList<>();
        this.entryPoints = new ArrayUnorderedList<>();
        this.items = new ArrayUnorderedList<>();
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

    public UnorderedListADT<IDivision> findBestPathToTarget(IDivision currentDivision, IDivision targetDivision) {
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
                }
            }
        }

        UnorderedListADT<IDivision> bestPath = new ArrayUnorderedList<>();
        IDivision step = targetDivision;
        while (step != null) {
            bestPath.addToFront(step);
            int predecessorIndex = visited.find(step);
            step = predecessors.getElement(predecessorIndex);
        }

        return bestPath;
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

        // Subtract points from enemies
        for (IEnemy enemy : this.getEnemiesByDivision(division)) {
            impact -= enemy.getPower();
        }

        return impact;
    }

    private int calculatePoints(UnorderedListADT<IDivision> path) {
        int points = 0;
        for (IDivision division : path) {
            // Considerar os itens e inimigos da divisão
            UnorderedListADT<IItem> items = this.getItemsByDivision(division);
            UnorderedListADT<IEnemy> enemies = this.getEnemiesByDivision(division);

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

}

