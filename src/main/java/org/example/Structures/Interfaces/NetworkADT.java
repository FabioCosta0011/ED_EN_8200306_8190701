package org.example.Structures.Interfaces;

public interface NetworkADT<T> extends GraphADT<T>
{
    /** Inserts an edge between two vertices of this graph. */
    public void addEdge (T vertex1, T vertex2, double weight);

    /** Returns the weight of the shortest path in this network. */
    public double shortestPathWeight(T vertex1, T vertex2);
}