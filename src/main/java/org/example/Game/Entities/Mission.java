package org.example.Game.Entities;

import org.example.Game.Entities.Interfaces.IDivision;
import org.example.Game.Entities.Interfaces.IEnemy;
import org.example.Game.Entities.Interfaces.IItem;
import org.example.Game.Entities.Interfaces.ITarget;
import org.example.Structures.Implementations.ArrayUnorderedList;
import org.example.Structures.Interfaces.UnorderedListADT;

public class Mission {

    private final String missionCode;

    private final int version;

    private final BuildingGraph<IDivision> divisions;

    private final UnorderedListADT<IEnemy> enemies;

    private final UnorderedListADT<IDivision> entryPoints;

    private final ITarget target;

    private final UnorderedListADT<IItem> items;

    public Mission(String missionCode, int version, ITarget target) {
        this.divisions = new BuildingGraph<>();
        this.missionCode = missionCode;
        this.version = version;
        this.enemies = new ArrayUnorderedList<>();
        this.entryPoints = new ArrayUnorderedList<>();
        this.target = target;
        this.items = new ArrayUnorderedList<>();
    }

    public String getMissionCode() {
        return missionCode;
    }

    public int getVersion() {
        return version;
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

    public UnorderedListADT<IItem> getItems() {
        return items;
    }

    public ArrayUnorderedList<IEnemy> getEnemiesByDivision(IDivision division) {
        ArrayUnorderedList<IEnemy> enemiesInDivision = new ArrayUnorderedList<>();

        // Iterar sobre todos os inimigos e adicionar à lista de resultados se pertencerem à divisão
        for (IEnemy enemy : enemies) {
            if (enemy.getCurrentDivision().equals(division)) {
                enemiesInDivision.addToRear(enemy);
            }
        }

        return enemiesInDivision;
    }

    public ArrayUnorderedList<IItem> getItemsByDivision(IDivision division) {
        ArrayUnorderedList<IItem> itemsInDivision = new ArrayUnorderedList<>();

        // Iterar sobre todos os itens e adicionar à lista de resultados se pertencerem à divisão
        for (IItem item : items) {
            if (item.getDivision().equals(division)) {
                itemsInDivision.addToRear(item);
            }
        }

        return itemsInDivision;
    }



}
