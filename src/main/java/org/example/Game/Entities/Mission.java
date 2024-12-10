package org.example.Game.Entities;

import org.example.Game.Entities.Interfaces.*;
import org.example.Structures.Implementations.ArrayUnorderedList;
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

    public void setMissionCode(String missionCode){
        this.missionCode = missionCode;
    }

    public int getVersion() {
        return version;
    }

    public void setMissionVersion(int version){
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

    public void setMissionTarget(ITarget target){
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

    public UnorderedListADT<IEnemy> getAllEnemies() {
        return enemies;
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



}

