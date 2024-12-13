package org.example.Game.Entities.Interfaces;

import java.util.Date;

public interface IRecord {

    /**
     * Get the date of the record.
     *
     * @return the date of the record.
     */
    Date getDate();

    /**
     * Get the health points of the record.
     *
     * @return the health points of the record.
     */
    int getHealthPoints();
}
