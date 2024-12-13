package org.example.Game.Entities;

import org.example.Structures.Implementations.ArrayUnorderedList;
import org.example.Structures.Implementations.Network;

/**
 * The Building class extends the Network class, representing a specific implementation of a graph structure
 * for buildings in the game. It provides methods to manage vertices (buildings), check for their existence,
 * retrieve neighbors (connected buildings), and generate string representations of the graph.
 *
 * @param <T> The type of the vertices in the graph (can be any object representing a building).
 */
public class Building<T> extends Network<T> {

    /**
     * Constructs a new Building object by calling the constructor of the parent Network class.
     */
    public Building() {
        super();
    }


    /**
     * Checks if the graph contains the specified vertex.
     *
     * @param vertex The vertex to check for existence.
     * @return true if the vertex exists in the graph, false otherwise.
     */
    public boolean containsVertex(T vertex) {
        for (int i = 0; i < this.numVertices; i++) {
            if (vertices[i].equals(vertex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a list of all vertices in the graph.
     *
     * @return An unordered list containing all vertices in the graph.
     */
    public ArrayUnorderedList<T> getAllVertices() {
        ArrayUnorderedList<T> vertexList = new ArrayUnorderedList<>();
        for (int i = 0; i < numVertices; i++) {
            vertexList.addToRear(vertices[i]);
        }
        return vertexList;
    }

    /**
     * Checks if a vertex exists in the graph and throws an exception if not.
     *
     * @param vertex The vertex to check for existence.
     * @throws IllegalArgumentException if the vertex does not exist.
     */
    private void checkVertexExists(T vertex) {
        if (!containsVertex(vertex)) {
            throw new IllegalArgumentException("Vertex doesn't exist");
        }
    }

    /**
     * Retrieves a list of neighboring vertices for a given vertex.
     *
     * @param vertex The vertex to find neighbors for.
     * @return An unordered list of neighboring vertices.
     * @throws IllegalArgumentException if the vertex does not exist.
     */
    public ArrayUnorderedList<T> getNeighbors(T vertex) {
        checkVertexExists(vertex);
        ArrayUnorderedList<T> neighbors = new ArrayUnorderedList<>();
        int index = getIndex(vertex);
        for (int i = 0; i < numVertices; i++) {
            if (networkAdjMatrix[index][i] != Double.POSITIVE_INFINITY) {
                neighbors.addToRear(vertices[i]);
            }
        }
        return neighbors;
    }

    /**
     * Checks if a given index is within the valid range of vertex indices.
     *
     * @param index The index to check for validity.
     * @return true if the index is valid, false otherwise.
     */
    public boolean indexIsValid(int index) {
        return index >= 0 && index < numVertices;
    }


    /**
     * Appends neighboring vertices to a StringBuilder object for string representation.
     *
     * @param result       The StringBuilder object to append the neighbors to.
     * @param vertexIndex The index of the vertex to find neighbors for.
     */
    private void appendNeighbors(StringBuilder result, int vertexIndex) {
        for (int j = 0; j < numVertices; j++) {
            if (networkAdjMatrix[vertexIndex][j] != Double.POSITIVE_INFINITY) {
                result.append(vertices[j]).append(" ");
            }
        }
    }

    /**
     * Generates a string representation of the graph, showing vertices and their neighbors.
     *
     * @return A string representation of the graph.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numVertices; i++) {
            result.append(vertices[i]).append(" -> ");
            appendNeighbors(result, i);
            result.append("\n");
        }
        return result.toString();
    }
}