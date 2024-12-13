package org.example.Game.Entities;

import org.example.Game.Entities.Interfaces.IRecord;

import java.util.Date;

/**
 * The Record class represents a record of a player's health points at a specific point in time.
 * It stores the date of the record and the health points associated with it.
 * This class also implements the Comparable interface to allow comparison between records based on health points.
 */
public class Record implements IRecord, Comparable<Record> {

    /**
     * The date when the record was created, representing the time of the event.
     */
    private final Date date;

    /**
     * The health points associated with the record, indicating the health status at the time.
     */
    private final int healthPoints;

    /**
     * Constructs a new Record with a specified date and health points.
     *
     * @param date the date of the record.
     * @param healthPoints the health points associated with the record.
     */
    public Record(Date date, int healthPoints) {
        this.date = date;
        this.healthPoints = healthPoints;
    }

    /**
     * Returns the date of the record.
     *
     * @return the date of the record.
     */
    @Override
    public Date getDate() {
        return date;
    }

    /**
     * Returns the health points of the record.
     *
     * @return the health points of the record.
     */
    @Override
    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * Compares this record with another record based on health points.
     * The comparison is done in ascending order of health points.
     *
     * @param other the record to compare with.
     * @return a negative integer, zero, or a positive integer as this record's health points
     *         are less than, equal to, or greater than the specified record's health points.
     */
    @Override
    public int compareTo(Record other) {
        return Integer.compare(this.healthPoints, other.healthPoints);
    }
}
