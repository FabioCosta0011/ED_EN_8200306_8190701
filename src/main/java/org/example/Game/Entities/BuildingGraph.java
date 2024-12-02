package org.example.Game.Entities;

import org.example.Game.Entities.Interfaces.IDivision;
import org.example.Structures.Implementations.ArrayUnorderedList;
import org.example.Structures.Implementations.Graph;
import org.example.Structures.Implementations.Network;

public class BuildingGraph<T> extends Network<T> {

    public BuildingGraph() {
        super();
    }


    /**
     * Replaces an old vertex with a new one.
     *
     * @param oldVertex The old vertex to be replaced.
     * @param newVertex The new vertex to replace the old one.
     * @throws IllegalArgumentException if the old vertex does not exist.
     */
    public void replaceVertex(T oldVertex, T newVertex) {
        int index = getIndex(oldVertex);
        if (indexIsValid(index)) {
            vertices[index] = newVertex;
        } else {
            throw new IllegalArgumentException("Vertex does not exist");
        }
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
     * Checks if an edge exists between two vertices.
     *
     * @param indexFrom The index of the source vertex.
     * @param indexTo   The index of the destination vertex.
     * @return true if an edge exists between the two vertices, false otherwise.
     */
    private boolean edgeExists(int indexFrom, int indexTo) {
        return networkAdjMatrix[indexFrom][indexTo] != Double.POSITIVE_INFINITY || networkAdjMatrix[indexTo][indexFrom] != Double.POSITIVE_INFINITY;
    }

    /**
     * Checks if an edge exists between two vertices.
     *
     * @param from The source vertex.
     * @param to   The destination vertex.
     * @return true if an edge exists between the two vertices, false otherwise.
     */
    public boolean containsEdge(T from, T to) {
        int indexFrom = getIndex(from);
        int indexTo = getIndex(to);
        return indexIsValid(indexFrom) && indexIsValid(indexTo) && edgeExists(indexFrom, indexTo);
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
     * Retrieves the adjacency matrix representing the edges in the graph.
     *
     * @return The adjacency matrix containing edge weights.
     */
    public double[][] getEdges() {
        return networkAdjMatrix;
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
     * Updates the graph with the minimum spanning tree (MST) obtained from another graph.
     *
     * @param mst The graph representing the minimum spanning tree.
     */
    @SuppressWarnings("unchecked")
    public void updateWithMST(Graph<IDivision> mst) {
        for (Object vertex : mst.getVertices()) {
            if (vertex instanceof IDivision) {
                this.addVertex((T) vertex);
            } else {
                System.out.println("Vertex isn't a Location");
            }
        }
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

    /**
     * Validates the indices of two vertices to ensure they are within the valid range.
     *
     * @param indexFrom The index of the source vertex.
     * @param indexTo   The index of the destination vertex.
     * @throws IllegalArgumentException if either index is invalid.
     */
    private void validateVertexIndices(int indexFrom, int indexTo) {
        if (!indexIsValid(indexFrom) || !indexIsValid(indexTo)) {
            throw new IllegalArgumentException("Invalid vertices");
        }
    }

    /**
     * Retrieves the weight of the edge between two vertices.
     *
     * @param from The source vertex.
     * @param to   The destination vertex.
     * @return The weight of the edge between the two vertices.
     * @throws IllegalArgumentException if either vertex does not exist.
     */
    public double getEdgeWeight(T from, T to) {
        int indexFrom = getIndex(from);
        int indexTo = getIndex(to);
        validateVertexIndices(indexFrom, indexTo);
        return networkAdjMatrix[indexFrom][indexTo];
    }
}