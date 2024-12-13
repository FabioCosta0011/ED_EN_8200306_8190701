package org.example.Game.Entities;

import org.example.Game.Entities.Interfaces.IRecord;

import java.util.Date;

public class Record implements IRecord, Comparable<Record> {

    private Date date;
    private int healthPoints;

    public Record(Date date, int healthPoints) {
        this.date = date;
        this.healthPoints = healthPoints;
    }

    @Override
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    @Override
    public int compareTo(Record other) {
        return Integer.compare(this.healthPoints, other.healthPoints);
    }
}
