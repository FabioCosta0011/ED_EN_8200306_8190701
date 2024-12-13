package org.example.Game.Entities.Interfaces;

import org.example.Game.Entities.BuildingGraph;
import org.example.Structures.Interfaces.OrderedListADT;
import org.example.Structures.Interfaces.QueueADT;
import org.example.Structures.Interfaces.UnorderedListADT;

public interface IMission {

    /**
     * This method returns the mission code
     *
     * @return the mission code
     */
    public String getMissionCode();

    /**
     * This method sets the mission code
     *
     * @param missionCode the mission code
     */
    public void setMissionCode(String missionCode);

    /**
     * This method returns the mission version
     *
     * @return the mission version
     */
    public int getVersion();

    /**
     * This method sets the mission version
     *
     * @param version the mission version
     */
    public void setMissionVersion(int version);

    /**
     * This method returns the divisions in the mission
     *
     * @return the divisions in the graph
     */
    public BuildingGraph<IDivision> getDivisions();

    /**
     * This method gets the enemies in the mission
     *
     * @return UnorderedListADT<IEnemy> unordered list of enemies
     */
    public UnorderedListADT<IEnemy> getEnemies();

    /**
     * This method gets the entry points in the mission
     *
     * @return UnorderedListADT<IDivision> unordered list of entry points
     */
    public UnorderedListADT<IDivision> getEntryPoints();

    /**
     * This method gets the target in the mission
     *
     * @return ITarget the target
     */
    public ITarget getTarget();

    /**
     * This method gets the points used to find best path
     *
     * @return the points
     */
    public int getPoints();

    /**
     * This method sets the points used to find best path
     *
     * @param points the points
     */
    public void setPoints(int points);

    /**
     * This method gets the records history of the mission
     *
     * @return OrderedListADT<IRecord> ordered list of records
     */
    public OrderedListADT<IRecord> getRecords();

    /**
     * This method sets the target of the current mission
     *
     * @param target the target
     */
    public void setMissionTarget(ITarget target);

    /**
     * This method gets the items in the mission
     *
     * @return UnorderedListADT<IItem> with the items
     */
    public UnorderedListADT<IItem> getItems();

    /**
     * This method gets the life kits in the mission
     *
     * @return UnorderedListADT<ILifeKit> with the life kits
     */
    public UnorderedListADT<IEnemy> getEnemiesByDivision(IDivision division);

    /**
     * This method, verify is the given division is equals to toCruz current division, and gives all enemies outside the current division
     *
     * @return UnorderedListADT<IEnemy> with the enemies outside the current division
     */
    public UnorderedListADT<IEnemy> getAllEnemiesOutsideCurrentDivision(IDivision division, IToCruz toCruz);

    /**
     * This method gets the items inside the given mission
     *
     * @return UnorderedListADT<IItem> with the items by given division
     */
    public UnorderedListADT<IItem> getItemsByDivision(IDivision division);

    /**
     * This method finds the best path between two divisions
     *
     * @param currentDivision the current division
     * @param targetDivision the target division
     *
     * @return QueueADT<IDivision> with the divisions who creates the best path
     */
    public QueueADT<IDivision> findBestPath(IDivision currentDivision, IDivision targetDivision);

    /**
     * This method finds the best path to the life kit
     *
     * @param currentDivision the current division
     *
     * @return QueueADT<IDivision> with the divisions who creates the best path
     */
    public UnorderedListADT<IDivision> findBestPathToLifeKit(IDivision currentDivision);

    /**
     * This method finds the best path to a specific division, considering multiple entry points
     *
     * @param entryPoints the unordered list of entry points
     * @param targetDivision the target division
     *
     * @return QueueADT<IDivision> with the divisions who creates the best path
     */
    public QueueADT<IDivision> findBestRouteFromMultipleEntryPoints(UnorderedListADT<IDivision> entryPoints, IDivision targetDivision);

    /**
     * This method verifies if is possible to return to the entry point safely
     *
     * @param pathQueue the queue with the path
     * @param initialPoints the initial points
     *
     * @return QueueADT<IDivision> with the divisions who creates the path to return to the entry point
     */
    public QueueADT<IDivision> verifyPathToEntry(QueueADT<IDivision> pathQueue, int initialPoints);

}