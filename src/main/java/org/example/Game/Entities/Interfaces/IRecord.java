package org.example.Game.Entities.Interfaces;

import java.util.Date;

public interface IRecord {

    /**
     * Get the date of the record.
     *
     * @return the date of the record.
     */
    public Date getDate();

    /**
     * Get the health points of the record.
     *
     * @return the health points of the record.
     */
    public int getHealthPoints();
}
